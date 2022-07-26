package com.liverussia.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SpinRouletteResponseDto {
    private PrizeInfoResponseDto prizeInfo;
    private List<String> base64Images;
    private String countElementsInOneSpin;
    private String spinDurationInMillis;
    private BigDecimal balance;
}
