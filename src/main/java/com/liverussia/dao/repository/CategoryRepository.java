package com.liverussia.dao.repository;

import com.liverussia.dao.entity.roulette.item.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
