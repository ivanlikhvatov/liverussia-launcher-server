package com.liverussia.service.impl;

import com.liverussia.domain.CaptchaResponse;
import com.liverussia.service.CaptchaRestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaRestServiceImpl implements CaptchaRestService {

    private static final String SECRET_PARAM_NAME = "secret";
    private static final String CAPTCHA_RESPONSE_PARAM_NAME = "response";

    private final RestTemplate captchaRestTemplate;

    @Value("${google.recaptcha.verification.endpoint}")
    private String recaptchaEndpoint;

    @Value("${google.recaptcha.verification.secret}")
    private String recaptchaSecret;

    @Override
    public boolean validateCaptcha(String captchaResponse) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add(SECRET_PARAM_NAME, recaptchaSecret);
        params.add(CAPTCHA_RESPONSE_PARAM_NAME, captchaResponse);

        CaptchaResponse apiResponse = captchaRestTemplate.postForObject(recaptchaEndpoint, params, CaptchaResponse.class);

        return Objects.nonNull(apiResponse) && apiResponse.getSuccess();
    }
}
