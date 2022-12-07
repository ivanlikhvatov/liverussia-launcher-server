package com.liverussia.rest;

import com.liverussia.error.handler.rest.CaptchaServiceErrorHandler;
import com.liverussia.error.handler.rest.ResourceServiceErrorHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

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

    @Bean
    public RestTemplate resourceRestTemplate(RestTemplateBuilder restTemplateBuilder, ResourceServiceErrorHandler errorHandler) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        restTemplate.setErrorHandler(errorHandler);

        return restTemplate;
    }
}
