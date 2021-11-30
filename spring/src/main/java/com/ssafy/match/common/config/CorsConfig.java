package com.ssafy.match.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//            .allowedOrigins("http://jxy.me") // http://jxy.me/websocket-debug-tool/
                .allowCredentials(false)
                .allowedMethods("*")
                .allowedOriginPatterns("*")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}