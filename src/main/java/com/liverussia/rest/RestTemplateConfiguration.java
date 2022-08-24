package com.liverussia.rest;

import com.liverussia.error.handler.rest.CaptchaServiceErrorHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder();
    }

    @Bean
    public RestTemplate captchaRestTemplate(RestTemplateBuilder restTemplateBuilder, CaptchaServiceErrorHandler errorHandler) {

        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.setErrorHandler(errorHandler);

        return restTemplate;
    }
}
