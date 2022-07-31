package com.liverussia.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import com.liverussia.error.apiException.ApiException;
import com.liverussia.error.apiException.ErrorContainer;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum Role implements GrantedAuthority {
    ANDROID_USER("Пользователь лаунчера", 0),
    ADMIN_LEVEL_1("Администратор первого уровня", 1),
    ADMIN_LEVEL_2("Администратор второго уровня", 2),
    ADMIN_LEVEL_3("Администратор третьего уровня", 3),
    ADMIN_LEVEL_4("Администратор четвертого уровня", 4),
    ADMIN_LEVEL_5("Администратор пятого уровня", 5),
    ADMIN_LEVEL_6("Администратор шестого уровня", 6),
    ADMIN_LEVEL_7("Администратор седьмого уровня", 7),
    ADMIN_LEVEL_8("Администратор восьмого уровня", 8),
    ADMIN_LEVEL_9("Администратор девятого уровня", 9),
    ADMIN_LEVEL_10("Администратор десятого уровня", 10);

    private static final int NOT_ADMIN_LEVEL = 0;

    private final String description;

    private final int adminLevel;

    @Override
    public String getAuthority() {
        return name();
    }

    @JsonCreator
    public static Role getRoleByAdminLevel(Integer adminLevel) {

        if (adminLevel == null) {
            return Role.ANDROID_USER;
        }

        return Arrays.stream(values())
                .filter(item -> item.adminLevel == adminLevel)
                .findFirst()
                .orElseThrow(() -> new ApiException(ErrorContainer.BAD_REQUEST));
    }

    public static Set<Role> getAllRolesByAdminLevel(Integer adminLevel) {

        if (adminLevel == null || adminLevel == NOT_ADMIN_LEVEL) {
            return Set.of(Role.ANDROID_USER);
        }

        return Arrays.stream(values())
                .filter(item -> item.adminLevel > 0)
                .filter(item -> item.adminLevel <= adminLevel)
                .collect(Collectors.toSet());
    }

    public static Role fromDescriptionValue(String name) {
        return Arrays.stream(values())
                .filter(item -> item.description.equals(name))
                .findFirst()
                .orElseThrow(() -> new ApiException(ErrorContainer.BAD_REQUEST));
    }
}
