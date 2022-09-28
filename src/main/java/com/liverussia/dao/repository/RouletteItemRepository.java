package com.liverussia.dao.repository;

import com.liverussia.dao.entity.roulette.item.Category;
import com.liverussia.dao.entity.roulette.item.CategoryName;
import com.liverussia.dao.entity.roulette.item.RouletteItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouletteItemRepository extends JpaRepository<RouletteItem, Long> {
    List<RouletteItem> findAllByCategoryId(Long id);
}