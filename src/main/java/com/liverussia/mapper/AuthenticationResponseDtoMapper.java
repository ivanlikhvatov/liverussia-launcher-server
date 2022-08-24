package com.liverussia.mapper;

import com.liverussia.domain.JwtUser;
import com.liverussia.dto.response.AuthenticationResponseDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface AuthenticationResponseDtoMapper {

    @Mapping(target = "accessToken", source = "accessToken")
    @Mapping(target = "refreshToken", source = "refreshToken")
    AuthenticationResponseDto map(JwtUser jwtUser, String accessToken, String refreshToken);

    //TODO Поправить после того как будет разделение на базы данных по серверам
    @AfterMapping
    default void mappingAfter(@MappingTarget AuthenticationResponseDto authenticationResponseDto) {
        authenticationResponseDto.setServerName("Test Server");
    }
}
