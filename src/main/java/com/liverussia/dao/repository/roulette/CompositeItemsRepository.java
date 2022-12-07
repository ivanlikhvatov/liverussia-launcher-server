package com.liverussia.dao.repository.roulette;

import com.liverussia.dao.entity.roulette.compositeItem.CompositeElementType;
import com.liverussia.dao.entity.roulette.compositeItem.CompositeItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompositeItemsRepository extends JpaRepository<CompositeItemsEntity, Long> {
    List<CompositeItemsEntity> findAllByType(CompositeElementType type);
}
