package com.liverussia.service.impl;

import com.liverussia.dao.entity.user.RefreshToken;
import com.liverussia.dao.repository.RefreshTokenRepository;
import com.liverussia.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void putNewToken(String login, String refreshToken) {
        RefreshToken refreshTokenEntity = Optional.ofNullable(refreshTokenRepository.findByLogin(login))
                .orElse(buildRefreshToken(login, refreshToken));

        refreshTokenEntity.setToken(refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);
    }

    @Override
    public String getTokenByLogin(String login) {
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByLogin(login);
        return Optional.ofNullable(refreshTokenEntity)
                .map(RefreshToken::getToken)
                .orElse(null);
    }

    private RefreshToken buildRefreshToken(String login, String refreshToken) {
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setLogin(login);

        return refreshTokenEntity;
    }
}
