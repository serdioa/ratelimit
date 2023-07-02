package de.serdioa.ratelimit;

import java.util.Objects;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import lombok.Getter;


@Getter
public class ResourceRateLimiter {

    private final String key;
    private final BucketConfiguration configuration;
    private final Bucket bucket;


    public ResourceRateLimiter(final String key, final BucketConfiguration configuration) {
        this.key = Objects.requireNonNull(key, "key is required");
        this.configuration = Objects.requireNonNull(configuration, "configuration is required");

        this.bucket = this.configuration.buildBucket();
    }


    public RateLimitProbe tryConsume(final long tokens) {
        final ConsumptionProbe probe = this.bucket.tryConsumeAndReturnRemaining(tokens);
        return new RateLimitProbe(key, probe, configuration);
    }


    public RateLimitProbe tryConsume() {
        return this.tryConsume(1);
    }
}
