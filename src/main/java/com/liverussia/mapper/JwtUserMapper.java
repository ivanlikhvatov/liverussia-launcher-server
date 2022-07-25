package com.liverussia.mapper;

import com.liverussia.dao.entity.User;
import com.liverussia.domain.JwtUser;
import org.mapstruct.Mapper;

@Mapper
public interface JwtUserMapper {

    JwtUser map(User user);

}
