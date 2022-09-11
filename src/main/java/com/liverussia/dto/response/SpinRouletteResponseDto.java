package com.liverussia.dto.response;

import lombok.Data;

@Data
public class SpinRouletteResponseDto {
    private PrizeInfoResponseDto prizeInfo;
    private String countElementsInOneSpin;
    private String spinDurationInMillis;
}
