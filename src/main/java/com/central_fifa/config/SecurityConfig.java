package com.central_fifa.config;

import com.central_fifa.service.centralService.ApiKeyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public ApiKeyFilter apiKeyFilterBean() {
        return new ApiKeyFilter();
    }

    @Bean
    public FilterRegistrationBean<ApiKeyFilter> apiKeyFilter(ApiKeyFilter apiKeyFilter) {
        FilterRegistrationBean<ApiKeyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(apiKeyFilter);
        registrationBean.addUrlPatterns("/synchronization"); // Appliquer uniquement à cet endpoint.
        return registrationBean;
    }
}
