package com.liverussia.dao.repository;

import com.liverussia.dao.entity.roulette.compositeItem.CompositeElementType;
import com.liverussia.dao.entity.roulette.compositeItem.CompositeItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompositeItemsRepository extends JpaRepository<CompositeItems, Long> {
    List<CompositeItems> findAllByType(CompositeElementType type);
}
