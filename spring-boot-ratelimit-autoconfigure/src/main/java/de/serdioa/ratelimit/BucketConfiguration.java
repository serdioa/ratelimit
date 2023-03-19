package de.serdioa.ratelimit;

import java.time.Duration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.Data;


@Data
public class BucketConfiguration {

    private final long capacity;
    private final long initialTokens;
    private final long refillTokens;
    private final Duration refillPeriod;
    private final boolean greedy;


    /* package private */ BucketConfiguration(final long capacity, final Long initialTokens, final Long refillTokens,
            final Duration refillPeriod, final boolean greedy) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity (" + capacity + ") cannot be <= 0");
        }
        this.capacity = capacity;

        if (refillPeriod == null) {
            throw new IllegalArgumentException("refillPeriod cannot be null");
        }
        this.refillPeriod = refillPeriod;

        this.greedy = greedy;

        if (initialTokens != null && initialTokens <= 0) {
            throw new IllegalArgumentException("initialTokens (" + initialTokens + ") cannot be <= 0");
        }
        // If initial tokens are not specified, fall back on capacity, i.e. initially the bucket is full.
        this.initialTokens = (initialTokens != null ? initialTokens : capacity);

        if (refillTokens != null && refillTokens <= 0) {
            throw new IllegalArgumentException("refillTokens (" + refillTokens + ") cannot be <= 0");
        }
        // If refill tokens are not specified, fall back on capacity or max possible speed, i.e. fill in the bucket
        // as fast as possible.
        final long maxRefillTokens = refillPeriod.toNanos();
        if (refillTokens != null) {
            this.refillTokens = Math.min(refillTokens, maxRefillTokens);
        } else {
            this.refillTokens = Math.min(capacity, maxRefillTokens);
        }
    }


    public Bucket buildBucket() {
        final Refill refill = (this.greedy
                ? Refill.greedy(this.refillTokens, this.refillPeriod)
                : Refill.intervally(this.refillTokens, this.refillPeriod));
        Bandwidth bandwith = Bandwidth.classic(this.capacity, refill)
                .withInitialTokens(this.initialTokens);

        return Bucket.builder().addLimit(bandwith).build();
    }
}
