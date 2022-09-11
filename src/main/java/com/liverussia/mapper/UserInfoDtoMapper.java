package com.liverussia.mapper;

import com.liverussia.dao.entity.user.User;
import com.liverussia.dto.response.UserInfoDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface UserInfoDtoMapper {

    @Mapping(target = "username", source = "login")
    UserInfoDto map(User user);

    //TODO убрать когда добавлю базы под каждый сервер
    @AfterMapping
    default void mapAfter(@MappingTarget UserInfoDto userInfoDto, User user) {
        userInfoDto.setServerName("Test Server");
    }
}
