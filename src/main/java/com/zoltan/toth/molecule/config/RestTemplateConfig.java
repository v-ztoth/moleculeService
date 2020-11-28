package com.zoltan.toth.molecule.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    private static final int CONNECT_TIMEOUT = 25000;
    private static final int SOCKET_TIMEOUT = 25000;

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .setReadTimeout(Duration.ofMillis(SOCKET_TIMEOUT))
                .setConnectTimeout(Duration.ofMillis(CONNECT_TIMEOUT))
                .build();
    }
}
