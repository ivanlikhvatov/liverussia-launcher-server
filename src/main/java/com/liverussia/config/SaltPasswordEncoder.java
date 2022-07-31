package com.liverussia.config;

import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Setter
@Component
public class SaltPasswordEncoder implements PasswordEncoder {

    private String salt;

    @Override
    public String encode(CharSequence rawPassword) {
        return DigestUtils.sha256Hex(rawPassword + salt);
    }

    @SneakyThrows
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if(salt != null) {
            return DigestUtils.sha256Hex(rawPassword + salt).equalsIgnoreCase(encodedPassword);
        } else {
            return rawPassword.equals(encodedPassword);
        }
    }
}
