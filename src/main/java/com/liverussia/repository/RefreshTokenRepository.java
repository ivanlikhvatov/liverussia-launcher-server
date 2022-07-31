package com.liverussia.repository;

import com.liverussia.dao.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    RefreshToken findByLogin(String login);
}
