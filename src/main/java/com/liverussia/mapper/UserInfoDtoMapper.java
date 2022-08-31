package com.liverussia.mapper;

import com.liverussia.dao.entity.User;
import com.liverussia.dto.response.UserInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserInfoDtoMapper {

    @Mapping(target = "username", source = "login")
    UserInfoDto map(User user);
}
