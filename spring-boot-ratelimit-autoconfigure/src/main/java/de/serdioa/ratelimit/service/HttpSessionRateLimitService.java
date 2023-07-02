package de.serdioa.ratelimit.service;

import de.serdioa.ratelimit.BucketConfiguration;
import de.serdioa.ratelimit.BucketFactory;
import de.serdioa.ratelimit.RateLimitProbe;
import de.serdioa.ratelimit.ResourceRateLimiter;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;


public class HttpSessionRateLimitService {

    private static final String ATTRIBUTE_NAME_PREFIX = "rateLimit.bucket.";

    @Autowired
    private BucketFactory bucketFactory;

    @Autowired
    private HttpSession httpSession;


    public RateLimitProbe tryConsume(final String key) {
        return this.tryConsume(key, 1);
    }


    public RateLimitProbe tryConsume(final String key, final long tokens) {
        final String attributeName = ATTRIBUTE_NAME_PREFIX + key;

        ResourceRateLimiter rateLimiter = (ResourceRateLimiter) httpSession.getAttribute(attributeName);
        if (rateLimiter == null) {
            BucketConfiguration configuration = this.bucketFactory.getBucketConfiguration(key);
            rateLimiter = new ResourceRateLimiter(key, configuration);
            httpSession.setAttribute(attributeName, rateLimiter);
        }

        return rateLimiter.tryConsume(tokens);
    }
}
