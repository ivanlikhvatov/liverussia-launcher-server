package com.liverussia.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ServerImagesResponseDto {
    private List<String> base64Images;
}
