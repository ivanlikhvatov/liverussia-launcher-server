package com.liverussia.config;

import com.liverussia.domain.PossiblePrizeInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RouletteConfiguration {

    @Bean
    public List<PossiblePrizeInfo> possiblePrizesInfo() {
        return List.of(
                new PossiblePrizeInfo("golden_vip.png", 1),
                new PossiblePrizeInfo("additional_car_slot.png", 2),
                new PossiblePrizeInfo("experience.png", 3),
                new PossiblePrizeInfo("game_currency.png", 4),
                new PossiblePrizeInfo("bmw_m5.png", 5),
                new PossiblePrizeInfo("random_skin.png", 6),
                new PossiblePrizeInfo("silver_vip.png", 7),
                new PossiblePrizeInfo("all_licenses_package.png", 8),
                new PossiblePrizeInfo("random_weapon.png", 9),
                new PossiblePrizeInfo("random_backpack.png", 10),
                new PossiblePrizeInfo("military_id.png", 11),
                new PossiblePrizeInfo("live_coins.png", 12),
                new PossiblePrizeInfo("random_car.png", 13),
                new PossiblePrizeInfo("removing_one_warning.png", 14),
                new PossiblePrizeInfo("vip_bronze.png", 15),
                new PossiblePrizeInfo("lamborghini_huracan.png", 16),
                new PossiblePrizeInfo("mercedes_benz_gt_63_s.png", 17)
        );
    }
}
