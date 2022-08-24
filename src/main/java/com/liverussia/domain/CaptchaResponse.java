package com.liverussia.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CaptchaResponse {
    private Boolean success;

    private LocalDateTime challenge_ts;

    private String hostname;

    @JsonProperty("error-codes")
    private List<String> errorCodes;
}
