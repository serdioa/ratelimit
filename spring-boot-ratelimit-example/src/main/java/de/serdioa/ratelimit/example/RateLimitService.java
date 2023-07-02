package de.serdioa.ratelimit.example;

import de.serdioa.ratelimit.BucketConfiguration;
import de.serdioa.ratelimit.BucketFactory;
import de.serdioa.ratelimit.RateLimitProbe;
import de.serdioa.ratelimit.ResourceRateLimiter;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RateLimitService {

    private static final String ATTRIBUTE_NAME_PREFIX = "rateLimit.bucket.";

    @Autowired
    private BucketFactory bucketFactory;


    public RateLimitProbe tryConsume(final HttpSession session, final String key) {
        return this.tryConsume(session, key, 1);
    }


    public RateLimitProbe tryConsume(final HttpSession session, final String key, final long tokens) {
        final String attributeName = ATTRIBUTE_NAME_PREFIX + key;

        ResourceRateLimiter rateLimiter = (ResourceRateLimiter) session.getAttribute(attributeName);
        if (rateLimiter == null) {
            BucketConfiguration configuration = this.bucketFactory.getBucketConfiguration(key);
            rateLimiter = new ResourceRateLimiter(key, configuration);
            session.setAttribute(attributeName, rateLimiter);
        }

        return rateLimiter.tryConsume(tokens);
    }
}
