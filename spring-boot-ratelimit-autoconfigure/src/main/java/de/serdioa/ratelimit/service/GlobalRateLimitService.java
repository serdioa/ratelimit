package de.serdioa.ratelimit.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import de.serdioa.ratelimit.BucketConfiguration;
import de.serdioa.ratelimit.BucketFactory;
import de.serdioa.ratelimit.RateLimitProbe;
import de.serdioa.ratelimit.ResourceRateLimiter;
import org.springframework.beans.factory.annotation.Autowired;


public class GlobalRateLimitService {

    @Autowired
    private BucketFactory bucketFactory;

    private ConcurrentMap<String, ResourceRateLimiter> rateLimiters = new ConcurrentHashMap<>();


    public RateLimitProbe tryConsume(final String key) {
        return this.tryConsume(key, 1);
    }


    public RateLimitProbe tryConsume(final String key, final long tokens) {
        final ResourceRateLimiter rateLimiter = this.getRateLimiter(key);
        return rateLimiter.tryConsume(tokens);
    }


    private ResourceRateLimiter getRateLimiter(final String key) {
        return this.rateLimiters.computeIfAbsent(key, k -> {
            BucketConfiguration configuration = this.bucketFactory.getBucketConfiguration(k);
            return new ResourceRateLimiter(k, configuration);
        });
    }
}
