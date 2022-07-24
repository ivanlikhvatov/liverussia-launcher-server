package com.liverussia.security;

import com.liverussia.dao.entity.User;
import com.liverussia.domain.JwtAuthentication;
import com.liverussia.dto.request.JwtRequest;
import com.liverussia.dto.response.JwtResponse;
import com.liverussia.error.ApiException;
import com.liverussia.error.ErrorContainer;
import com.liverussia.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;

    private final AuthenticationManager authenticationManager;


    public JwtResponse login(JwtRequest request) {
        //TODO Вынести в userService выброс ошибки
        User user = userService.getByLogin(request.getLogin())
                .orElseThrow(() -> new ApiException(ErrorContainer.USER_NOT_FOUND));

        authenticateUser(request);

        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

        refreshStorage.put(user.getLogin(), refreshToken);

        return new JwtResponse(accessToken, refreshToken);
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

    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.getByLogin(login)
                        .orElseThrow(() -> new ApiException(ErrorContainer.USER_NOT_FOUND));
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.getByLogin(login)
                        .orElseThrow(() -> new ApiException(ErrorContainer.USER_NOT_FOUND));
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getLogin(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new ApiException(ErrorContainer.TOKEN_EXPIRED);
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}
