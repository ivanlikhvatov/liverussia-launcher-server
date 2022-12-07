package com.liverussia.dao.repository.roulette;

import com.liverussia.dao.entity.roulette.item.RouletteItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouletteItemRepository extends JpaRepository<RouletteItemEntity, Long> {
    List<RouletteItemEntity> findAllByCategoryId(Long id);
}