package de.serdioa.ratelimit.autoconfigure;

import de.serdioa.ratelimit.BucketFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration(proxyBeanMethods = false)
public class BucketFactoryAutoConfiguration {

    @Bean
    @ConfigurationProperties("request-rate-limit")
    public BucketFactory rateLimitBucketFactory() {
        return new BucketFactory();
    }
}
