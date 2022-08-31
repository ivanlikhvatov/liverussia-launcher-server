package com.liverussia.mapper;

import com.liverussia.domain.JwtUser;
import com.liverussia.dto.response.AuthenticationResponseDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Optional;

@Mapper
public interface AuthenticationResponseDtoMapper {

    @Mapping(target = "accessToken", source = "accessToken")
    @Mapping(target = "refreshToken", source = "refreshToken")
    @Mapping(target = "userInfo.balance", source = "jwtUser.balance")
    @Mapping(target = "userInfo.username", source = "jwtUser.login")
    @Mapping(target = "userInfo.serverName", source = "jwtUser.serverName")
    AuthenticationResponseDto map(JwtUser jwtUser, String accessToken, String refreshToken);

    //TODO Поправить после того как будет разделение на базы данных по серверам
    @AfterMapping
    default void mappingAfter(@MappingTarget AuthenticationResponseDto authenticationResponseDto) {
        Optional.ofNullable(authenticationResponseDto)
                .map(AuthenticationResponseDto::getUserInfo)
                .ifPresent(userInfoDto -> userInfoDto.setServerName("Test Server"));
    }
}
