package de.serdioa.ratelimit;

import java.time.Duration;
import java.util.Map;

import lombok.Setter;


public class BucketFactory {

    /**
     * Default capacity is unlimited, since there is no good generic default for a capacity. Initial and refill tokens
     * defaults to the capacity (whatever is the configured capacity).
     */
    @Setter
    private long defaultCapacity = Long.MAX_VALUE;

    /**
     * Default refill period is 1 minute.
     */
    @Setter
    private Duration defaultRefillPeriod = Duration.ofMinutes(1);

    /**
     * By default the greedy refill is used.
     */
    @Setter
    private boolean defaultGreedy = true;

    private final Map<String, Long> capacity = new HierarchicalProperties<>();
    private final Map<String, Long> initialTokens = new HierarchicalProperties<>();
    private final Map<String, Long> refillTokens = new HierarchicalProperties<>();
    private final Map<String, Duration> refillPeriod = new HierarchicalProperties<>();
    private final Map<String, Boolean> greedy = new HierarchicalProperties<>();


    public void addCapacity(final Map<String, Long> capacity) {
        this.capacity.putAll(capacity);
    }


    public void addCapacity(final String key, final Long capacity) {
        this.capacity.put(key, capacity);
    }


    public void addInitialTokens(final Map<String, Long> initialTokens) {
        this.initialTokens.putAll(initialTokens);
    }


    public void addInitialTokens(final String key, final Long initialTokens) {
        this.initialTokens.put(key, initialTokens);
    }


    public void addRefillTokens(final Map<String, Long> refillTokens) {
        this.refillTokens.putAll(refillTokens);
    }


    public void addRefillTokens(final String key, final Long refillTokens) {
        this.refillTokens.put(key, refillTokens);
    }


    public void addRefillPeriod(final Map<String, Duration> refillPeriod) {
        this.refillPeriod.putAll(refillPeriod);
    }


    public void addRefillPeriod(final String key, final Duration refillPeriod) {
        this.refillPeriod.put(key, refillPeriod);
    }


    public void addGreedy(final Map<String, Boolean> greedy) {
        this.greedy.putAll(greedy);
    }


    public void addGreedy(final String key, final Boolean greedy) {
        this.greedy.put(key, greedy);
    }


    private long getCapacity(final String key) {
        return this.capacity.getOrDefault(key, this.defaultCapacity);
    }


    private Long getInitialTokens(final String key) {
        return this.initialTokens.get(key);
    }


    private Long getRefillTokens(final String key) {
        return this.refillTokens.get(key);
    }


    private Duration getRefillPeriod(final String key) {
        return this.refillPeriod.getOrDefault(key, this.defaultRefillPeriod);
    }


    private boolean isGreedy(final String key) {
        return this.greedy.getOrDefault(key, this.defaultGreedy);
    }


    public BucketConfiguration getBucketConfiguration(final String key) {
        final long bucketCapacity = this.getCapacity(key);
        final Long bucketInitialTokens = this.getInitialTokens(key);
        final Long bucketRefillTokens = this.getRefillTokens(key);
        final Duration bucketRefillPeriod = this.getRefillPeriod(key);
        final boolean bucketGreedy = this.isGreedy(key);

        return new BucketConfiguration(bucketCapacity,
                bucketInitialTokens,
                bucketRefillTokens,
                bucketRefillPeriod,
                bucketGreedy);
    }
}
