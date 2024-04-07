package com.alipay.authserver.config;

import com.alipay.authserver.interceptor.AuthAccessTokenInterceptor;
import com.alipay.authserver.interceptor.LoginInterceptor;
import com.alipay.authserver.interceptor.OauthInterceptor;
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

    @Bean
    public OauthInterceptor oauthInterceptor() {
        return new OauthInterceptor();
    }

    @Bean
    public AuthAccessTokenInterceptor accessTokenInterceptor() {
        return new AuthAccessTokenInterceptor();
    }

    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/user/**", "/oauth2.0/authorizePage", "/oauth2.0/authorize", "/sso/token");
        registry.addInterceptor(oauthInterceptor()).addPathPatterns("/oauth2.0/authorize");
        registry.addInterceptor(accessTokenInterceptor()).addPathPatterns("/api/**");
    }


}