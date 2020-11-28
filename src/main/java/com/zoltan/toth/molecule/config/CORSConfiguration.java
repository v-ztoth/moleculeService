package com.zoltan.toth.molecule.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "cors")
public class CORSConfiguration implements WebMvcConfigurer {

    private static final boolean ACCESS_CONTROL_ALLOW_CREDENTIAL_VALUE = true;
    private static final String[] EXPOSED_HEADERS = {"origin","content-type","accept","authorization", "location"};

    private static final long maxAge = 36000;

    private String allowedOrigins;

    public String getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(final String allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(ACCESS_CONTROL_ALLOW_CREDENTIAL_VALUE)
                .allowedMethods("GET","POST","PUT","OPTIONS","HEAD","DELETE")
                .exposedHeaders(EXPOSED_HEADERS)
                .allowedHeaders(EXPOSED_HEADERS)
                .maxAge(maxAge)
                .allowedOrigins(allowedOrigins);
    }
}

