package de.serdioa.ratelimit;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;


public class BucketFactoryStandalone {

    public static void main(String[] args) throws Exception {
        BucketFactory f = new BucketFactory();
        //f.setDefaultGreedy(false);

        f.addGreedy("aaa", true);
        f.addCapacity("aaa", 20L);
        // f.addRefillTokens("aaa", 30L);
        BucketConfiguration configuration = f.getBucketConfiguration("aaa");
        Bucket b = configuration.buildBucket();

        while (!Thread.currentThread().isInterrupted()) {
            ConsumptionProbe probe = b.tryConsumeAndReturnRemaining(1);
            System.out.println("Consumed ? " + probe.isConsumed());
            System.out.println("Wait to refill (ms): " + probe.getNanosToWaitForRefill() / 1_000_000);
            System.out.println("Wait to reset (ms): " + probe.getNanosToWaitForReset() / 1_000_000);
            System.out.println("Remaining tokens: " + probe.getRemainingTokens());
            System.out.println("Refill period: " + configuration.getRefillPeriod());

            System.out.println("RateLimit-Limit: " + RateLimitHttpHeaderBuilder.limit(configuration, probe));
            System.out.println("RateLimit-Policy: " + RateLimitHttpHeaderBuilder.policy(configuration));
            System.out.println("RateLimit-Remaining: " + RateLimitHttpHeaderBuilder.remaining(probe));
            System.out.println("RateLimit-Reset: " + RateLimitHttpHeaderBuilder.reset(probe));
            if (!probe.isConsumed()) {
                System.out.println("Retry-After: " + RateLimitHttpHeaderBuilder.retryAfter(probe));
            }

            System.out.println();

            Thread.sleep(1000);
        }
    }
}
