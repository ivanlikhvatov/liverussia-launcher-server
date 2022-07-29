package com.liverussia.config;

import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


//TODO Сделать компонентнои
public class SaltPasswordEncoder implements PasswordEncoder {

    public static String SALT = "";

    @Override
    public String encode(CharSequence rawPassword) {
        return DigestUtils.sha256Hex(rawPassword.toString());
    }

    @SneakyThrows
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String salt = SALT;

/**
 * If salt is null, it means the value in constants object is empty now.
 * => client-secret validation is going on
 * => we just need the comparision of plain-text string.
 */
        if(salt != null) {

//            String encode = DigestUtils.sha256Hex(salt + rawPassword);
//
//            byte[] bytesDbEncodePassword = encodedPassword.getBytes(StandardCharsets.UTF_8);
//
////    case of password authentication
//
//            byte[] bytesInputEncodePassword = getHashWithSalt(rawPassword.toString().toUpperCase(), salt.getBytes(StandardCharsets.UTF_8));
//
//            boolean tmp = DigestUtils.sha256Hex(rawPassword + salt).equalsIgnoreCase(encodedPassword);
//
//            return true;

            String encode = DigestUtils.sha256Hex(rawPassword + salt);

            boolean tmp = DigestUtils.sha256Hex(rawPassword + salt).equalsIgnoreCase(encodedPassword);

            return tmp;

        } else {
            //case of client-secret authentication
            return rawPassword.equals(encodedPassword);
        }
    }

    public byte[] getHashWithSalt(String input, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(salt);
        byte[] hashedBytes = digest.digest(stringToByte(input));
        return hashedBytes;
    }

    public byte[] stringToByte(String input) {
        if (Base64.isBase64(input)) {
            return Base64.decodeBase64(input);

        } else {
            return Base64.encodeBase64(input.getBytes());
        }
    }
}
