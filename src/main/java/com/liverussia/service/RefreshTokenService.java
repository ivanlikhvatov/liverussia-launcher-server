package com.liverussia.service;

public interface RefreshTokenService {

    void putNewToken(String login, String refreshToken);

    String getTokenByLogin(String login);
}
