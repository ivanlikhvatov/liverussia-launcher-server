package com.liverussia.service.impl;

import com.liverussia.dao.entity.roulette.compositeItem.CompositeItemsEntity;
import com.liverussia.dao.entity.roulette.item.CategoryEntity;
import com.liverussia.dao.entity.roulette.item.RouletteItemEntity;
import com.liverussia.dao.entity.roulette.item.RouletteItemType;
import com.liverussia.dao.entity.roulette.rangeItem.RangeElementType;
import com.liverussia.dao.entity.roulette.singleItem.SingleElementType;
import com.liverussia.dao.entity.user.RoulettePrizes;
import com.liverussia.dao.entity.user.User;
import com.liverussia.dao.repository.roulette.CategoryRepository;
import com.liverussia.dao.repository.roulette.CompositeItemsRepository;
import com.liverussia.dao.repository.roulette.RouletteItemRepository;
import com.liverussia.dao.repository.user.RoulettePrizesRepository;
import com.liverussia.domain.JwtUser;
import com.liverussia.domain.roulette.CompositeItemData;
import com.liverussia.domain.roulette.RangeItemData;
import com.liverussia.domain.roulette.RouletteItem;
import com.liverussia.domain.roulette.SingleItemData;
import com.liverussia.dto.response.PrizeInfoResponseDto;
import com.liverussia.dto.response.SpinRouletteResponseDto;
import com.liverussia.error.apiException.ApiException;
import com.liverussia.error.apiException.ErrorContainer;
import com.liverussia.service.ResourceRestService;
import com.liverussia.service.RouletteService;
import com.liverussia.service.UserService;
import com.liverussia.utils.Base64Converter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
public class RouletteServiceImpl implements RouletteService {

    private final static long ZERO = 0L;
    private final static int BIG_DECIMAL_COMPARE_EQUALS_RESULT = 0;
    private final static long ZERO_PROBABILITY = 0L;
    private final static long ONE_HUNDRED_PERCENT = 100;
    private final static int COUNT_ITEM_BEFORE_PRIZE = 2;
    private final static String EMPTY_PRIZE_VALUE = "0";

    private final UserService userService;
    private final RouletteItemRepository rouletteItemRepository;
    private final CompositeItemsRepository compositeItemsRepository;
    private final CategoryRepository categoryRepository;
    private final RoulettePrizesRepository roulettePrizesRepository;
    private final ResourceRestService resourceRestService;

    @Value("${android.roulette.spinCost}")
    private BigDecimal spinCost;

    @Value("${android.roulette.countElementsInOneSpin}")
    private Integer countElementsInOneSpin;

    @Value("${android.roulette.spinDurationInMillis}")
    private Long spinDurationInMillis;


    @Override
    public SpinRouletteResponseDto spinRoulette(JwtUser jwtUser) {
        User user = userService.getUserByLogin(jwtUser.getLogin());
        checkUserBalance(user);

        List<RouletteItem> rouletteItems = getRouletteItems();

        SpinRouletteResponseDto response = buildRouletteResponseDto();
        response.setBase64Images(getRouletteItemsImages(rouletteItems));
        addPrizeToResponseAndSave(response, rouletteItems, user);

        return response;
    }

    private void addPrizeToResponseAndSave(SpinRouletteResponseDto response, List<RouletteItem> rouletteItems, User user) {
        RouletteItem prizeItem = rouletteItems.get(rouletteItems.size() - COUNT_ITEM_BEFORE_PRIZE);

        if (RouletteItemType.SINGLE.equals(prizeItem.getType())) {
            handleSingleType(response, prizeItem, user);
        }

        if (RouletteItemType.RANGE.equals(prizeItem.getType())) {
            handleRangeType(response, prizeItem, user);
        }

        if (RouletteItemType.COMPOSITE.equals(prizeItem.getType())) {
            handleCompositeType(response, prizeItem, user);
        }

        processRouletteSpinPay(user);
        response.setBalance(user.getBalance());
    }

    private void processRouletteSpinPay(User user) {
        BigDecimal newBalance = user.getBalance().subtract(spinCost);
        user.setBalance(newBalance);
        userService.saveUser(user);
    }

    private void handleCompositeType(SpinRouletteResponseDto response, RouletteItem rouletteItem, User user) {
        CompositeItemData compositeItemData = rouletteItem.getCompositeItemData();
        CompositeItemsEntity compositeItem = getCompositeItem(compositeItemData);

        String base64PrizeImage = getCompositePrizeImage(compositeItem);
        PrizeInfoResponseDto prizeInfo = new PrizeInfoResponseDto();
        prizeInfo.setBase64Image(base64PrizeImage);

        saveCompositeTypePrize(compositeItem, user);

        response.setPrizeInfo(prizeInfo);
    }

    private void saveCompositeTypePrize(CompositeItemsEntity compositeItem, User user) {
        savePrizeToDb(compositeItem.getType().getSampType(), compositeItem.getSampElementId(), user.getId());
    }

    private String getCompositePrizeImage(CompositeItemsEntity compositeItem) {
        return Optional.ofNullable(compositeItem)
                .map(CompositeItemsEntity::getImageFileName)
                .map(resourceRestService::getPrizeInfoImageFile)
                .map(Base64Converter::encodeFileToBase64)
                .orElse(StringUtils.EMPTY);
    }

    private CompositeItemsEntity getCompositeItem(CompositeItemData compositeItemData) {
        List<CompositeItemsEntity> compositeItems = compositeItemsRepository.findAllByType(compositeItemData.getType());

        Random rand = new Random();
        int index = rand.nextInt(compositeItems.size());

        return compositeItems.get(index);
    }

    private void handleRangeType(SpinRouletteResponseDto response, RouletteItem rouletteItem, User user) {
        String base64PrizeImage = getRangePrizeImage(rouletteItem);
        String rangeRandomValue = getRangeRandomValue(rouletteItem);

        PrizeInfoResponseDto prizeInfo = new PrizeInfoResponseDto();
        prizeInfo.setBase64Image(base64PrizeImage);
        prizeInfo.setAdditionalInfo(rangeRandomValue);

        response.setPrizeInfo(prizeInfo);

        saveRangeTypePrize(rouletteItem, rangeRandomValue, user);
    }

    private void saveRangeTypePrize(RouletteItem rouletteItem, String rangeRandomValue, User user) {
        RangeElementType type = getRangeItemType(rouletteItem);
        savePrizeToDb(type.getSampType(), rangeRandomValue, user.getId());
    }

    private RangeElementType getRangeItemType(RouletteItem rouletteItem) {
        return Optional.ofNullable(rouletteItem)
                .map(RouletteItem::getRangeItemData)
                .map(RangeItemData::getRangeElementType)
                .orElseThrow(() -> new ApiException(ErrorContainer.OTHER));
    }

    private String getRangeRandomValue(RouletteItem rouletteItem) {
        RangeItemData rangeItemData = rouletteItem.getRangeItemData();
        Long start = rangeItemData.getRangeStart();
        Long end = rangeItemData.getRangeEnd();

        long value = start + (long) (Math.random() * (end - start));

        return Long.toString(value);
    }

    private String getRangePrizeImage(RouletteItem rouletteItem) {
        return Optional.ofNullable(rouletteItem)
                .map(RouletteItem::getRangeItemData)
                .map(RangeItemData::getPrizeImageFileName)
                .map(resourceRestService::getPrizeInfoImageFile)
                .map(Base64Converter::encodeFileToBase64)
                .orElse(StringUtils.EMPTY);
    }

    private void handleSingleType(SpinRouletteResponseDto response, RouletteItem rouletteItem, User user) {
        String base64PrizeImage = getSinglePrizeImage(rouletteItem);
        PrizeInfoResponseDto prizeInfo = new PrizeInfoResponseDto();
        prizeInfo.setBase64Image(base64PrizeImage);
        response.setPrizeInfo(prizeInfo);

        saveSingleTypePrize(rouletteItem, user);
    }

    private void saveSingleTypePrize(RouletteItem rouletteItem, User user) {
        SingleElementType type = getSingleItemType(rouletteItem);

        if (SingleElementType.CAR.equals(type)) {
            String sampId = getSingleItemSampId(rouletteItem);
            savePrizeToDb(SingleElementType.CAR.getSampType(), sampId, user.getId());
        } else {
            savePrizeToDb(type.getSampType(), type.getSampValue(), user.getId());
        }
    }

    private String getSingleItemSampId(RouletteItem rouletteItem) {
        return Optional.ofNullable(rouletteItem)
                .map(RouletteItem::getSingleItemData)
                .map(SingleItemData::getSampElementId)
                .orElseThrow(() -> new ApiException(ErrorContainer.OTHER));
    }

    private SingleElementType getSingleItemType(RouletteItem rouletteItem) {
        return Optional.ofNullable(rouletteItem)
                .map(RouletteItem::getSingleItemData)
                .map(SingleItemData::getSingleElementType)
                .orElseThrow(() -> new ApiException(ErrorContainer.OTHER));
    }

    private String getSinglePrizeImage(RouletteItem rouletteItem) {
        return Optional.ofNullable(rouletteItem)
                .map(RouletteItem::getSingleItemData)
                .map(SingleItemData::getPrizeImageFileName)
                .map(resourceRestService::getPrizeInfoImageFile)
                .map(Base64Converter::encodeFileToBase64)
                .orElse(StringUtils.EMPTY);
    }

    private List<String> getRouletteItemsImages(List<RouletteItem> rouletteItems) {
        return Optional.ofNullable(rouletteItems)
                .orElse(Collections.emptyList())
                .stream()
                .map(RouletteItem::getImageFile)
                .map(Base64Converter::encodeFileToBase64)
                .collect(Collectors.toList());
    }

    private List<RouletteItem> getRouletteItems() {

        List<RouletteItem> rouletteItems = new ArrayList<>();

        List<CategoryEntity> categories = categoryRepository.findAll();

        Optional.of(categories)
                .orElse(Collections.emptyList())
                .forEach(category -> addItemsByCategory(category, rouletteItems));

        Collections.shuffle(rouletteItems);

        return rouletteItems;

    }

    private void addItemsByCategory(CategoryEntity category, List<RouletteItem> rouletteItems) {
        List<RouletteItemEntity> rouletteItemsByCategoryEntity = rouletteItemRepository.findAllByCategoryId(category.getId());
        List<RouletteItem> rouletteItemsByCategory = resourceRestService.getRouletteItemsWithImages(rouletteItemsByCategoryEntity);

        Collections.shuffle(rouletteItemsByCategory);

        long residualCount = getItemCount(category);

        for (int i = 0; i < rouletteItemsByCategory.size(); i++) {
            if (residualCount == 0L) {
                break;
            }

            long value;

            if (i == rouletteItemsByCategory.size() - 1) {
                value = residualCount;
            } else {
                value = (long) (Math.random() * residualCount);
            }

            residualCount -= value;

            addNeedCountsItems(rouletteItems, rouletteItemsByCategory.get(i), value);
        }
    }

    private void addNeedCountsItems(List<RouletteItem> rouletteItems, RouletteItem item, long itemCount) {
        LongStream.range(ZERO, itemCount)
                .forEach(index -> rouletteItems.add(item));
    }

    private long getItemCount(CategoryEntity category) {
        long probability = getProbability(category);
        return countElementsInOneSpin / ONE_HUNDRED_PERCENT * probability;
    }

    private long getProbability(CategoryEntity category) {
        return Optional.ofNullable(category)
                .map(CategoryEntity::getPercentProbability)
                .orElse(ZERO_PROBABILITY);
    }

    private void checkUserBalance(User user) {
        if (user.getBalance() == null) {
            throw new ApiException(ErrorContainer.NOT_ENOUGH_MONEY);
        }


        if (user.getBalance().compareTo(spinCost) < BIG_DECIMAL_COMPARE_EQUALS_RESULT) {
            throw new ApiException(ErrorContainer.NOT_ENOUGH_MONEY);
        }
    }

    private SpinRouletteResponseDto buildRouletteResponseDto() {
        SpinRouletteResponseDto responseDto = new SpinRouletteResponseDto();

        responseDto.setCountElementsInOneSpin(countElementsInOneSpin.toString());
        responseDto.setSpinDurationInMillis(spinDurationInMillis.toString());

        return responseDto;
    }

    private void savePrizeToDb(String type, String value, String userId) {

        if (StringUtils.isBlank(value)) {
            value = EMPTY_PRIZE_VALUE;
        }

        RoulettePrizes roulettePrizes = buildRoulettePrizeEntity(type, value, userId);
        roulettePrizesRepository.save(roulettePrizes);
    }

    private RoulettePrizes buildRoulettePrizeEntity(String type, String value, String userId) {

        RoulettePrizes roulettePrize = new RoulettePrizes();
        roulettePrize.setType(type);
        roulettePrize.setValue(value);
        roulettePrize.setUserId(userId);

        return roulettePrize;
    }
}
