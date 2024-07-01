package com.steam.steamimitator.config;

import com.steam.steamimitator.services.openai.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {

    @Value("${rate.limiter.permits-per-minute}")
    private double permitsPerMinute;

    @Bean
    public RateLimiter rateLimiter() {
        return new RateLimiter(permitsPerMinute);
    }
}
