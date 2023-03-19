package de.serdioa.ratelimit;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;


public class BucketFactoryStandalone {

    public static void main(String[] args) throws Exception {
        BucketFactory f = new BucketFactory();
        //f.setDefaultGreedy(false);

        f.addGreedy("aaa", false);
        f.addCapacity("aaa", 20L);
        // f.addRefillTokens("aaa", 30L);
        Bucket b = f.getBucketConfiguration("aaa").buildBucket();

        while (!Thread.currentThread().isInterrupted()) {
            ConsumptionProbe probe = b.tryConsumeAndReturnRemaining(1);
            System.out.println("Consumed ? " + probe.isConsumed());
            System.out.println("Wait to refill (ms): " + probe.getNanosToWaitForRefill() / 1_000_000);
            System.out.println("Wait to reset (ms): " + probe.getNanosToWaitForReset() / 1_000_000);
            System.out.println("Remaining tokens: " + probe.getRemainingTokens());
            System.out.println();

            Thread.sleep(1000);
        }
    }
}
