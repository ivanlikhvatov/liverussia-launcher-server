package com.liverussia.service;

import com.liverussia.dao.entity.roulette.item.RouletteItemEntity;
import com.liverussia.domain.roulette.RouletteItem;

import java.io.File;
import java.util.List;

public interface ResourceRestService {
    List<RouletteItem> getRouletteItemsWithImages(List<RouletteItemEntity> rouletteItemEntities);
    byte[] getPrizeInfoImageFile(String filename);
}
