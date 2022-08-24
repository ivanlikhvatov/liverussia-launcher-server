package com.liverussia.service.impl;

import com.liverussia.domain.CaptchaResponse;
import com.liverussia.service.CaptchaRestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaRestServiceImpl implements CaptchaRestService {

    private final RestTemplate captchaRestTemplate;

    @Value("${google.recaptcha.verification.endpoint}")
    private String recaptchaEndpoint;

    @Value("${google.recaptcha.verification.secret}")
    private String recaptchaSecret;

    @Override
    public boolean validateCaptcha(String captchaResponse) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("secret", recaptchaSecret);
        params.add("response", captchaResponse);

        CaptchaResponse apiResponse = captchaRestTemplate.postForObject(recaptchaEndpoint, params, CaptchaResponse.class);

        return Objects.nonNull(apiResponse) && apiResponse.getSuccess();
    }

//    private <Response, Request> Response sendRequest(URI url, MultiValueMap<String, String> body, HttpMethod httpMethod, Class<Response> responseEntityClass) {
//        HttpEntity<?> httpEntity = new HttpEntity<>(body, buildHeaders());
//        ResponseEntity<Response> response = captchaRestTemplate.exchange(url, httpMethod, httpEntity, responseEntityClass);
//        return response.getBody();
//    }
//
//    private HttpHeaders buildHeaders() {
//        HttpHeaders httpHeaders = new HttpHeaders();
//        return httpHeaders;
//    }
}
