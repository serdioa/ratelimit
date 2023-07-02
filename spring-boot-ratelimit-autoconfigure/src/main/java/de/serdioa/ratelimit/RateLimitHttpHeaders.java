package de.serdioa.ratelimit;

import io.github.bucket4j.ConsumptionProbe;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


/**
 * HTTP headers used to describe rate limit policy.
 * <p>
 * Rate limit headers are not part of the HTTP standard as of 2023 yet, although many major companies and services, such
 * as GitHub and Twitter, are using them. Currently there is a
 * <a href="https://greenbytes.de/tech/webdav/draft-ietf-httpapi-ratelimit-headers-latest.html">draft</a>
 * to standartise the headers.
 * <p>
 * Although these headers are not standard yet, according to
 * <a href="https://www.rfc-editor.org/rfc/rfc6648">RFC 6648</a> even non-standard headers should NOT be prefixed with
 * "X-", as it was a common practice in the past.
 */
@NoArgsConstructor(access = AccessLevel.NONE)
public class RateLimitHttpHeaders {

    public static final String LIMIT = "RateLimit-Limit";
    public static final String POLICY = "RateLimit-Policy";
    public static final String REMAINING = "RateLimit-Remaining";
    public static final String RESET = "RateLimit-Reset";


    public static String limit(RateLimitProbe probe) {
        return limit(probe.getConfiguration(), probe.getProbe());
    }


    public static String limit(BucketConfiguration configuration, ConsumptionProbe probe) {
        // Since the initial number of tokens may be higher than the capacity to give a client a head-start,
        // use the maximum of capacity and remainig tokens to determine the limit in the current time window.
        final long capacity = configuration.getCapacity();
        final long remainingTokens = probe.getRemainingTokens();

        final long limit = Math.max(capacity, remainingTokens);
        return String.valueOf(limit);
    }


    public static String policy(RateLimitProbe probe) {
        return policy(probe.getConfiguration());
    }


    public static String policy(BucketConfiguration configuration) {
        final long capacity = configuration.getCapacity();

        // The HTTP header supports only an integer number of seconds, but the configured refill period
        // may be less than 1 second. In such case, round it up to 1 second.
        final long refillPeriodSeconds = Math.max(1, configuration.getRefillPeriod().getSeconds());

        final StringBuilder sb = new StringBuilder(32);
        sb.append(capacity).append(";w=").append(refillPeriodSeconds);
        if (configuration.isGreedy()) {
            sb.append(";type=sliding");
        } else {
            sb.append(";type=interval");
        }

        return sb.toString();
    }


    public static String remaining(RateLimitProbe probe) {
        return remaining(probe.getProbe());
    }


    public static String remaining(ConsumptionProbe probe) {
        final long remainingTokens = probe.getRemainingTokens();
        return String.valueOf(remainingTokens);
    }


    public static String reset(RateLimitProbe probe) {
        return reset(probe.getProbe());
    }


    public static String reset(ConsumptionProbe probe) {
        final long secondsToWaitForReset = probe.getNanosToWaitForReset() / 1_000_000_000;
        return String.valueOf(secondsToWaitForReset);
    }


    public static String retryAfter(RateLimitProbe probe) {
        return retryAfter(probe.getProbe());
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
