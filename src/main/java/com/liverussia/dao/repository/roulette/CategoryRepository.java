package com.liverussia.dao.repository.roulette;

import com.liverussia.dao.entity.roulette.item.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
