package com.example.oauth2.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@Configuration
public class WebConfigurer {
    private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);
    private final AppProperties properties;

    public WebConfigurer(AppProperties properties) {
        this.properties = properties;
    }

    /**
     * Enable CORS so that our frontend client can access the APIs from a different origin
     */
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = properties.getCors();
        if (configuration.getAllowedOrigins() != null && !configuration.getAllowedOrigins().isEmpty()){
            log.debug("Registering CORS filter ");
            urlBasedCorsConfigurationSource.registerCorsConfiguration("/api/**", configuration);
            urlBasedCorsConfigurationSource.registerCorsConfiguration("/v2/api-docs", configuration);
        }
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}
