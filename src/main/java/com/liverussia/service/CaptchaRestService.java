package com.liverussia.service;

public interface CaptchaRestService {
    boolean validateCaptcha(String captchaResponse);
}
