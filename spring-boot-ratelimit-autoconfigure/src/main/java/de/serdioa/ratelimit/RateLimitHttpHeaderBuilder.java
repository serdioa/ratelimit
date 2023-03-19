package de.serdioa.ratelimit;

import io.github.bucket4j.ConsumptionProbe;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


/**
 * Builds HTTP headers related to rate limiting.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RateLimitHttpHeaderBuilder {

    public static String limit(BucketConfiguration configuration, ConsumptionProbe probe) {
        // Since the initial number of tokens may be higher than the capacity to give a client a head-start,
        // use the maximum of capacity and remainig tokens to determine the limit in the current time window.
        final long capacity = configuration.getCapacity();
        final long remainingTokens = probe.getRemainingTokens();

        final long limit = Math.max(capacity, remainingTokens);
        return String.valueOf(limit);
    }


    public static String policy(BucketConfiguration configuration) {
        final long capacity = configuration.getCapacity();

        // The HTTP header supports only an integer number of seconds, but the configured refill period
        // may be less than 1 second. In such case, round it up to 1 second.
        final long refillPeriodSeconds = Math.max(1, configuration.getRefillPeriod().getSeconds());

        return String.valueOf(capacity) + ";w=" + String.valueOf(refillPeriodSeconds);
    }


    public static String remaining(ConsumptionProbe probe) {
        final long remainingTokens = probe.getRemainingTokens();
        return String.valueOf(remainingTokens);
    }


    public static String reset(ConsumptionProbe probe) {
        final long secondsToWaitForReset = probe.getNanosToWaitForReset() / 1_000_000_000;
        return String.valueOf(secondsToWaitForReset);
    }


    public static String retryAfter(ConsumptionProbe probe) {
        // The HTTP header contains an integer number of seconds, whereas internally time is tracked with
        // the nanoseconds precision.
        // Transform time to wait from nanoseconds to seconds, and add 1 to account for rounding.
        // For example, if tokens will become available after 3.456 seconds, we return 4 seconds.
        final long secondsToWaitForRefill = 1 + probe.getNanosToWaitForRefill() / 1_000_000_000;
        return String.valueOf(secondsToWaitForRefill);
    }
}
