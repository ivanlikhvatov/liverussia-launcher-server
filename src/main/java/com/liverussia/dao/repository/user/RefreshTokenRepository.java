package com.liverussia.dao.repository.user;

import com.liverussia.dao.entity.user.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByLogin(String login);
}
