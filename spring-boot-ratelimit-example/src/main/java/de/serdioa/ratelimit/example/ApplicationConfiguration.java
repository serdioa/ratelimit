package de.serdioa.ratelimit.example;

import de.serdioa.ratelimit.BucketFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ApplicationConfiguration {

    @Bean
    @ConfigurationProperties("web.rate-limit")
    public BucketFactory rateLimitBucketFactory() {
        return new BucketFactory();
    }
}
