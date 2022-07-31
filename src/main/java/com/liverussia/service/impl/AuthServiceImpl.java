package com.liverussia.service.impl;

import com.liverussia.domain.JwtUser;
import com.liverussia.dto.request.JwtRequest;
import com.liverussia.dto.response.JwtResponse;
import com.liverussia.error.apiException.ApiException;
import com.liverussia.error.apiException.ErrorContainer;
import com.liverussia.security.JwtProvider;
import com.liverussia.service.AuthService;
import com.liverussia.service.RefreshTokenService;
import com.liverussia.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;

    private final AuthenticationManager authenticationManager;

    @Override
    public JwtResponse login(JwtRequest request) {
        JwtUser jwtUser = userService.getJwtUserByLogin(request.getLogin());

        authenticateUser(request);

        String accessToken = jwtProvider.generateAccessToken(jwtUser);
        String refreshToken = jwtProvider.generateRefreshToken(jwtUser);

        refreshTokenService.putNewToken(jwtUser.getLogin(), refreshToken);

        return new JwtResponse(accessToken, refreshToken);
    }

    @Override
    public JwtResponse getAccessToken(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            String login = claims.getSubject();
            String saveRefreshToken = refreshTokenService.getTokenByLogin(login);

            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                JwtUser user = userService.getJwtUserByLogin(login);
                String accessToken = jwtProvider.generateAccessToken(user);

                return new JwtResponse(accessToken, null);
            }
        }

        return new JwtResponse(null, null);
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            String login = claims.getSubject();
            String saveRefreshToken = refreshTokenService.getTokenByLogin(login);

            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                JwtUser user = userService.getJwtUserByLogin(login);

                String accessToken = jwtProvider.generateAccessToken(user);
                String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshTokenService.putNewToken(user.getLogin(), newRefreshToken);

                return new JwtResponse(accessToken, newRefreshToken);
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
