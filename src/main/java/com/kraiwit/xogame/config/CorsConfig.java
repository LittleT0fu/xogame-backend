package com.kraiwit.xogame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

        @Bean
        public CorsFilter corsFilter() {
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                CorsConfiguration config = new CorsConfiguration();

                // Allow credentials (cookies, authorization headers)
                config.setAllowCredentials(true);

                // Allow specific origins (replace with your frontend URL)
                config.setAllowedOrigins(Arrays.asList(
                                "http://localhost:5173",
                                "http://localhost:3000",
                                "http://localhost:4200",
                                "https://yourdomain.com"));

                // Or allow all origins (not recommended for production)
                // config.addAllowedOriginPattern("*");

                // Allow specific headers
                config.setAllowedHeaders(Arrays.asList(
                                "Origin",
                                "Content-Type",
                                "Accept",
                                "Authorization",
                                "X-Requested-With"));

                // Allow specific HTTP methods
                config.setAllowedMethods(Arrays.asList(
                                "GET",
                                "POST",
                                "PUT",
                                "DELETE",
                                "PATCH",
                                "OPTIONS"));

                // How long the response from a pre-flight request can be cached (in seconds)
                config.setMaxAge(3600L);

                // Apply CORS configuration to all endpoints
                source.registerCorsConfiguration("/**", config);

                return new CorsFilter(source);
        }
}