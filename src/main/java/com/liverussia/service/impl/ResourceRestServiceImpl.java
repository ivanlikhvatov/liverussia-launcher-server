package com.liverussia.service.impl;

import com.liverussia.dao.entity.roulette.item.RouletteItemEntity;
import com.liverussia.domain.roulette.RouletteItem;
import com.liverussia.mapper.RouletteItemMapper;
import com.liverussia.service.ResourceRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResourceRestServiceImpl implements ResourceRestService {

    private final static String SLASH = "/";

    @Value("${resources.host}")
    private String host;

    @Value("${resources.rouletteItems.path}")
    private String rouletteItemsPath;

    @Value("${resources.prizesInfo.path}")
    private String prizesInfoPath;

    private final RouletteItemMapper rouletteItemMapper;
    private final RestTemplate resourceRestTemplate;

    @Override
    public List<RouletteItem> getRouletteItemsWithImages(List<RouletteItemEntity> rouletteItemEntities) {

        List<RouletteItem> rouletteItems = rouletteItemMapper.map(rouletteItemEntities);

        return Optional.ofNullable(rouletteItems)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::enrichRouletteItemByImageFile)
                .collect(Collectors.toList());
    }

    private RouletteItem enrichRouletteItemByImageFile(RouletteItem rouletteItem) {
        byte[] file = getRouletteItemImage(rouletteItem.getImageFileName());
        rouletteItem.setImageFile(file);
        return rouletteItem;
    }

    private byte[] getRouletteItemImage(String filename) {
        String uri = host.concat(rouletteItemsPath)
                .concat(SLASH)
                .concat(filename);

        return resourceRestTemplate.getForObject(uri, byte[].class);
    }

    @Override
    public byte[] getPrizeInfoImageFile(String filename) {
        String uri = host.concat(prizesInfoPath)
                .concat(SLASH)
                .concat(filename);

        return resourceRestTemplate.getForObject(uri, byte[].class);
    }
}
