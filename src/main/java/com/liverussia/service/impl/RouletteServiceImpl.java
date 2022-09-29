package com.liverussia.service.impl;

import com.liverussia.dao.entity.roulette.compositeItem.CompositeItemData;
import com.liverussia.dao.entity.roulette.compositeItem.CompositeItems;
import com.liverussia.dao.entity.roulette.item.Category;
import com.liverussia.dao.entity.roulette.item.RouletteItem;
import com.liverussia.dao.entity.roulette.item.RouletteItemType;
import com.liverussia.dao.entity.roulette.rangeItem.RangeElementType;
import com.liverussia.dao.entity.roulette.rangeItem.RangeItemData;
import com.liverussia.dao.entity.roulette.singleItem.SingleElementType;
import com.liverussia.dao.entity.roulette.singleItem.SingleItemData;
import com.liverussia.dao.entity.user.Donate;
import com.liverussia.dao.entity.user.RoulettePrizes;
import com.liverussia.dao.entity.user.User;
import com.liverussia.dao.repository.*;
import com.liverussia.domain.JwtUser;
import com.liverussia.dto.response.PrizeInfoResponseDto;
import com.liverussia.dto.response.SpinRouletteResponseDto;
import com.liverussia.error.apiException.ApiException;
import com.liverussia.error.apiException.ErrorContainer;
import com.liverussia.service.RouletteService;
import com.liverussia.service.UserService;
import com.liverussia.utils.Base64Converter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
public class RouletteServiceImpl implements RouletteService {

    //TODO вынести это в yml
    private final static String ROULETTE_ITEMS_DIRECTORY = "roulette_items/";
    private final static String PRIZES_INFO_DIRECTORY = "prizes_info/";

    private final static long ZERO = 0L;
    private final static long ZERO_PROBABILITY = 0L;
    private final static long ONE_HUNDRED_PERCENT = 100;
    private final static int COUNT_ITEM_BEFORE_PRIZE = 2;

    private final UserService userService;
    private final RouletteItemRepository rouletteItemRepository;
    private final CompositeItemsRepository compositeItemsRepository;
    private final CategoryRepository categoryRepository;
    private final DonateRepository donateRepository;
    private final RoulettePrizesRepository roulettePrizesRepository;

    @Value("${android.roulette.spinCost}")
    private Integer spinCost;

    @Value("${android.roulette.countElementsInOneSpin}")
    private Integer countElementsInOneSpin;

    @Value("${android.roulette.spinDurationInMillis}")
    private Long spinDurationInMillis;

    @Value("${upload.path}")
    private String uploadPath;


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
        Donate donate = donateRepository.findByUserId(user.getId());
        Integer newBalance = donate.getBalance() - spinCost;
        donate.setBalance(newBalance);
        user.setBalance(newBalance.toString());

        donateRepository.save(donate);
    }

    private void handleCompositeType(SpinRouletteResponseDto response, RouletteItem rouletteItem, User user) {
        CompositeItemData compositeItemData = rouletteItem.getCompositeItemData();
        CompositeItems compositeItem = getCompositeItem(compositeItemData);

        String base64PrizeImage = getCompositePrizeImage(compositeItem);
        PrizeInfoResponseDto prizeInfo = new PrizeInfoResponseDto();
        prizeInfo.setBase64Image(base64PrizeImage);

        saveCompositeTypePrize(compositeItem, user);

        response.setPrizeInfo(prizeInfo);
    }

    private void saveCompositeTypePrize(CompositeItems compositeItem, User user) {
        savePrizeToDb(compositeItem.getType().getSampType(), compositeItem.getSampElementId(), user.getId());
    }

    private String getCompositePrizeImage(CompositeItems compositeItem) {
        String path = uploadPath.concat(PRIZES_INFO_DIRECTORY);

        return Optional.ofNullable(compositeItem)
                .map(CompositeItems::getImageFileName)
                .map(path::concat)
                .map(Base64Converter::encodeFileToBase64)
                .orElse(StringUtils.EMPTY);
    }

    private CompositeItems getCompositeItem(CompositeItemData compositeItemData) {
        List<CompositeItems> compositeItems = compositeItemsRepository.findAllByType(compositeItemData.getType());

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
        String path = uploadPath.concat(PRIZES_INFO_DIRECTORY);

        return Optional.ofNullable(rouletteItem)
                .map(RouletteItem::getRangeItemData)
                .map(RangeItemData::getPrizeImageFileName)
                .map(path::concat)
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
        }

        savePrizeToDb(type.getSampType(), type.getSampValue(), user.getId());
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
        String path = uploadPath.concat(PRIZES_INFO_DIRECTORY);

        return Optional.ofNullable(rouletteItem)
                .map(RouletteItem::getSingleItemData)
                .map(SingleItemData::getPrizeImageFileName)
                .map(path::concat)
                .map(Base64Converter::encodeFileToBase64)
                .orElse(StringUtils.EMPTY);
    }

    private List<String> getRouletteItemsImages(List<RouletteItem> rouletteItems) {
        String path = uploadPath.concat(ROULETTE_ITEMS_DIRECTORY);

        return Optional.ofNullable(rouletteItems)
                .orElse(Collections.emptyList())
                .stream()
                .map(RouletteItem::getImageFileName)
                .map(path::concat)
                .map(Base64Converter::encodeFileToBase64)
                .collect(Collectors.toList());
    }

    private List<RouletteItem> getRouletteItems() {

        List<RouletteItem> rouletteItems = new ArrayList<>();

        List<Category> categories = categoryRepository.findAll();

        Optional.of(categories)
                .orElse(Collections.emptyList())
                .forEach(category -> addItemsByCategory(category, rouletteItems));

        Collections.shuffle(rouletteItems);

        return rouletteItems;

    }

    private void addItemsByCategory(Category category, List<RouletteItem> rouletteItems) {
        List<RouletteItem> rouletteItemsByCategory = rouletteItemRepository.findAllByCategoryId(category.getId());
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

    private long getItemCount(Category category) {
        long probability = getProbability(category);
        return countElementsInOneSpin / ONE_HUNDRED_PERCENT * probability;
    }

    private long getProbability(Category category) {
        return Optional.ofNullable(category)
                .map(Category::getPercentProbability)
                .orElse(ZERO_PROBABILITY);
    }

    private void checkUserBalance(User user) {
        if (StringUtils.isBlank(user.getBalance())) {
            throw new ApiException(ErrorContainer.NOT_ENOUGH_MONEY);
        }

        long balance = Long.parseLong(user.getBalance());

        if (balance < spinCost) {
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
            value = null;
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
