package com.platform.donation_service.configs;

import com.platform.donation_service.context.HeaderPropagationInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;

/**
 * Configuration class for RestTemplate settings.
 */
@Configuration
public class RestTemplateConfig {
    /**
     * Timeout duration for the RestTemplate in milliseconds.
     */
    private static final int TIMEOUT = 1000;

    /**
     * Creates a RestTemplate bean with custom timeout settings.
     *
     * @param builder the RestTemplateBuilder used to configure the RestTemplate
     * @param headerInterceptor the interceptor to propagate headers,
     *                          from RequestContext
     * @return a RestTemplate instance with the specified timeout settings
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder,
                                     HeaderPropagationInterceptor headerInterceptor) {
        return builder
                .setConnectTimeout(Duration.ofMillis(TIMEOUT))
                .setReadTimeout(Duration.ofMillis(TIMEOUT))
                .interceptors(List.of(headerInterceptor))
                .build();
    }
}
