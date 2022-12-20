package com.liverussia.service.impl;

import com.liverussia.domain.JwtUser;
import com.liverussia.domain.Role;
import com.liverussia.dto.request.AdminAuthenticationRequestDto;
import com.liverussia.dto.request.AuthenticationRequestDto;
import com.liverussia.dto.response.AdminAuthenticationResponseDto;
import com.liverussia.dto.response.AuthenticationResponseDto;
import com.liverussia.error.apiException.ApiException;
import com.liverussia.error.apiException.ErrorContainer;
import com.liverussia.mapper.AuthenticationResponseDtoMapper;
import com.liverussia.security.JwtProvider;
import com.liverussia.service.AuthService;
import com.liverussia.service.CaptchaRestService;
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
    private final CaptchaRestService captchaRestService;

    private final AuthenticationManager authenticationManager;
    private final AuthenticationResponseDtoMapper authenticationResponseDtoMapper;

    @Override
    public AuthenticationResponseDto loginAndroidUser(AuthenticationRequestDto request) {
//        boolean isValidCaptcha = captchaRestService.validateCaptcha(request.getCaptchaToken());
//
//        if (!isValidCaptcha) {
//            throw new ApiException(ErrorContainer.CAPTCHA_ERROR);
//        }

        JwtUser jwtUser = userService.getJwtUserByLogin(request.getLogin());

        authenticateUser(request.getLogin(), request.getPassword(), jwtUser.getSalt());

        String accessToken = jwtProvider.generateAccessToken(jwtUser);
        String refreshToken = jwtProvider.generateRefreshToken(jwtUser);

        refreshTokenService.putNewToken(jwtUser.getLogin(), refreshToken);

        return authenticationResponseDtoMapper.mapAndroidUserResponse(jwtUser, accessToken, refreshToken);
    }

    @Override
    public AdminAuthenticationResponseDto loginAdminUser(AdminAuthenticationRequestDto request) {
        JwtUser jwtUser = userService.getJwtUserByLogin(request.getLogin());

        if(!jwtUser.getRoles().contains(Role.ADMIN_LEVEL_10)) {
            throw new ApiException(ErrorContainer.AUTHENTICATION_ERROR);
        }

        authenticateUser(request.getLogin(), request.getPassword(), jwtUser.getSalt());

        String accessToken = jwtProvider.generateAccessToken(jwtUser);
        String refreshToken = jwtProvider.generateRefreshToken(jwtUser);

        refreshTokenService.putNewToken(jwtUser.getLogin(), refreshToken);

        return authenticationResponseDtoMapper.mapAdminResponse(jwtUser, accessToken, refreshToken);
    }

    @Override
    public AuthenticationResponseDto getAccessToken(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            String login = claims.getSubject();
            String saveRefreshToken = refreshTokenService.getTokenByLogin(login);

            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                JwtUser user = userService.getJwtUserByLogin(login);
                String accessToken = jwtProvider.generateAccessToken(user);

                return authenticationResponseDtoMapper.mapAndroidUserResponse(user, accessToken, null);
            }
        }

        throw new ApiException(ErrorContainer.AUTHENTICATION_ERROR);
    }

    @Override
    public AuthenticationResponseDto refreshAndroidUserToken(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            String login = claims.getSubject();
            String saveRefreshToken = refreshTokenService.getTokenByLogin(login);

            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                JwtUser user = userService.getJwtUserByLogin(login);

                String accessToken = jwtProvider.generateAccessToken(user);
                String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshTokenService.putNewToken(user.getLogin(), newRefreshToken);

                return authenticationResponseDtoMapper.mapAndroidUserResponse(user, accessToken, newRefreshToken);
            }
        }

        throw new ApiException(ErrorContainer.AUTHENTICATION_ERROR);
    }

    @Override
    public AdminAuthenticationResponseDto refreshAdminUserToken(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            String login = claims.getSubject();
            String saveRefreshToken = refreshTokenService.getTokenByLogin(login);

            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                JwtUser user = userService.getJwtUserByLogin(login);

                String accessToken = jwtProvider.generateAccessToken(user);
                String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshTokenService.putNewToken(user.getLogin(), newRefreshToken);

                return authenticationResponseDtoMapper.mapAdminResponse(user, accessToken, newRefreshToken);
            }
        }

        throw new ApiException(ErrorContainer.AUTHENTICATION_ERROR);
    }

    private void authenticateUser(String login, String password, String salt) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(login, password.concat(salt));

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            throw new ApiException(ErrorContainer.USER_NOT_FOUND);
        }
    }

}
