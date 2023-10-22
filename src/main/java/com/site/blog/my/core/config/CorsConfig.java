package com.site.blog.my.core.config;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.context.annotation.*;
import org.springframework.web.filter.*;
import org.springframework.web.cors.*;
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
//        config.addAllowedOrigin("http://myblog-frontend.s3-website.ca-central-1.amazonaws.com");
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
