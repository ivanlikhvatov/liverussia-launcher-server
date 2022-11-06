package com.liverussia.config;

import com.liverussia.domain.ServerImageInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LauncherConfiguration {

    @Bean
    public List<ServerImageInfo> possiblePrizesInfo() {
        return List.of(
                new ServerImageInfo("golden_vip.png", 1),
                new ServerImageInfo("additional_car_slot.png", 2),
                new ServerImageInfo("experience.png", 3),
                new ServerImageInfo("game_currency.png", 4),
                new ServerImageInfo("bmw_m5.png", 5),
                new ServerImageInfo("random_skin.png", 6),
                new ServerImageInfo("silver_vip.png", 7),
                new ServerImageInfo("all_licenses_package.png", 8),
                new ServerImageInfo("random_weapon.png", 9),
                new ServerImageInfo("random_backpack.png", 10),
                new ServerImageInfo("military_id.png", 11),
                new ServerImageInfo("live_coins.png", 12),
                new ServerImageInfo("random_car.png", 13),
                new ServerImageInfo("removing_one_warning.png", 14),
                new ServerImageInfo("lamborghini_huracan.png", 15),
                new ServerImageInfo("mercedes_benz_gt_63_s.png", 16),
                new ServerImageInfo("random_mask.png", 17),
                new ServerImageInfo("platinum_vip.png", 18),
                new ServerImageInfo("random_cap.png", 19)
        );
    }


    @Bean
    public List<ServerImageInfo> donateServicesInfo() {
        return List.of(
                new ServerImageInfo("game_currency.png", 1),
                new ServerImageInfo("increase_car_slots.png", 2),
                new ServerImageInfo("all_licenses_package.png", 3),
                new ServerImageInfo("change_family_name.png", 4),
                new ServerImageInfo("removing_one_warning.png", 5),
                new ServerImageInfo("family_transfer_to_player.png", 6),
                new ServerImageInfo("increase_business_slots.png", 7),
                new ServerImageInfo("military_id.png", 8),
                new ServerImageInfo("increase_home_slots.png", 9),
                new ServerImageInfo("silver_vip.png", 10),
                new ServerImageInfo("golden_vip.png", 11),
                new ServerImageInfo("platinum_vip.png", 12)
        );
    }

    @Bean
    public List<String> loaderSliderTexts() {
        return List.of(
                "Главный Администратор выдает окончательный ответ в жалобах",
                "Администратор никогда не попросит Вашего пароля",
                "В нашей группе есть бот для привязки и восстановления доступа к аккаунту",
                "\"LIVE RUSSIA\" - один из крупных мобильных CRMP проектов в СНГ",
                "Вид от 1-го лица включается обычным свайпом двумя пальцами",
                "Машину можно купить не имея дома",
                "Администрация набирается через форум liverussia.online",
                "Основатель \"LIVE RUSSIA\" является ютубером"
        );
    }
}
