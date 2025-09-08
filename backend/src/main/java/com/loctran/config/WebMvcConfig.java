package com.loctran.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        CorsRegistration corsRegistration = registry.addMapping("/api/**");
        corsRegistration.allowedOriginPatterns("*");
        corsRegistration.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS"); // ← Explicit methods, not "*"
        corsRegistration.allowedHeaders("*");
        corsRegistration.allowCredentials(true);
    }
}
