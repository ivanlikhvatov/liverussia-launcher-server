package com.liverussia.dao.repository;

import com.liverussia.dao.entity.user.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    RefreshToken findByLogin(String login);
}
