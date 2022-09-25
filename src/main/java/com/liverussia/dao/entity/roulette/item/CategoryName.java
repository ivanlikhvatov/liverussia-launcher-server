package com.liverussia.dao.entity.roulette.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryName {
    NORMAL("Обычное"),
    MEDIUM("Среднее"),
    RARE("Редкое"),
    EPIC("Эпическое"),
    LEGENDARY("Легендарное");

    private final String clientValue;
}
