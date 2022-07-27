package com.liverussia.service.impl;

import com.liverussia.dao.entity.User;
import com.liverussia.domain.TokenAndExpiration;
import com.liverussia.dto.request.JwtRequest;
import com.liverussia.dto.response.JwtResponse;
import com.liverussia.error.apiException.ApiException;
import com.liverussia.error.apiException.ErrorContainer;
import com.liverussia.security.JwtProvider;
import com.liverussia.service.AuthService;
import com.liverussia.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;

    private final AuthenticationManager authenticationManager;

    @Override
    public JwtResponse login(JwtRequest request) {
        User user = userService.getByLogin(request.getLogin());

        authenticateUser(request);

        TokenAndExpiration accessToken = jwtProvider.generateAccessToken(user);
        TokenAndExpiration refreshToken = jwtProvider.generateRefreshToken(user);

        refreshStorage.put(user.getLogin(), refreshToken.getToken());

        //TODO маппер
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setAccessToken(accessToken.getToken());
        jwtResponse.setAccessExpiration(accessToken.getExpiration());
        jwtResponse.setRefreshToken(refreshToken.getToken());
        jwtResponse.setRefreshExpiration(refreshToken.getExpiration());

        return jwtResponse;
    }

    @Override
    public JwtResponse getAccessToken(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            String login = claims.getSubject();
            String saveRefreshToken = refreshStorage.get(login);

            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                User user = userService.getByLogin(login);
                TokenAndExpiration accessToken = jwtProvider.generateAccessToken(user);


                //TODO маппер
                JwtResponse jwtResponse = new JwtResponse();
                jwtResponse.setAccessToken(accessToken.getToken());
                jwtResponse.setAccessExpiration(accessToken.getExpiration());

                return jwtResponse;
            }
        }

        return new JwtResponse();
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            String login = claims.getSubject();
            String saveRefreshToken = refreshStorage.get(login);

            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                User user = userService.getByLogin(login);


                TokenAndExpiration accessToken = jwtProvider.generateAccessToken(user);
                TokenAndExpiration newRefreshToken = jwtProvider.generateRefreshToken(user);

                refreshStorage.put(user.getLogin(), newRefreshToken.getToken());

                //TODO маппер
                JwtResponse jwtResponse = new JwtResponse();
                jwtResponse.setAccessToken(accessToken.getToken());
                jwtResponse.setAccessExpiration(accessToken.getExpiration());
                jwtResponse.setRefreshToken(newRefreshToken.getToken());
                jwtResponse.setRefreshExpiration(newRefreshToken.getExpiration());

                return jwtResponse;
            }
        }

        throw new ApiException(ErrorContainer.AUTHENTICATION_ERROR);
    }

    private void authenticateUser(JwtRequest request) {
        String login = request.getLogin();
        String password = request.getPassword();

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(login, password);

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ErrorContainer.USER_NOT_FOUND);
        }
    }

}
