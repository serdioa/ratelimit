package de.serdioa.ratelimit.autoconfigure;

import de.serdioa.ratelimit.service.GlobalRateLimitService;
import de.serdioa.ratelimit.service.HttpSessionRateLimitService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration(proxyBeanMethods = false)
public class RateLimitServiceAutoConfiguration {

    @Bean
    public GlobalRateLimitService globalRateLimitService() {
        return new GlobalRateLimitService();
    }


    @Bean
    public HttpSessionRateLimitService httpSessionRateLimitService() {
        return new HttpSessionRateLimitService();
    }
}
