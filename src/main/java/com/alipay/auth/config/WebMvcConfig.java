package com.alipay.auth.config;

import com.alipay.auth.interceptor.AuthAccessTokenInterceptor;
import com.alipay.auth.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author hyy
 * @Description
 * @create 2024-04-04 15:53
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/oauth2.0/clientRegister", "/oauth2.0/authorize", "/sso/token");
        registry.addInterceptor(authAccessTokenInterceptor()).addPathPatterns("/api/**");
    }

    @Bean
    public AuthAccessTokenInterceptor authAccessTokenInterceptor() {
        return new AuthAccessTokenInterceptor();
    }

    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }
}