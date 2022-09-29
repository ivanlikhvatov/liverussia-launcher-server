package com.liverussia.dao.repository;

import com.liverussia.dao.entity.user.Donate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonateRepository extends JpaRepository<Donate, String> {
    Donate findByUserId(String userId);
}
