package com.liverussia.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServerImageInfo {
    private String fileName;
    private int position;
}
