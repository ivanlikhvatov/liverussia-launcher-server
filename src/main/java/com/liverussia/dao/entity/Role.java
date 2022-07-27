package com.liverussia.dao.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import com.liverussia.error.apiException.ApiException;
import com.liverussia.error.apiException.ErrorContainer;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Role implements GrantedAuthority {
    ANDROID_USER("Пользователь лаунчера"),
    ADMIN("Администратор");

    private final String description;

    @Override
    public String getAuthority() {
        return name();
    }

    @JsonCreator
    public static Role fromValue(String name) {
        return Arrays.stream(values())
                .filter(item -> item.description.equals(name))
                .findFirst()
                .orElseThrow(() -> new ApiException(ErrorContainer.BAD_REQUEST));
    }
}
