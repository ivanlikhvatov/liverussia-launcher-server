package com.liverussia.service;

import com.liverussia.dao.entity.User;
import com.liverussia.domain.JwtUser;
import com.liverussia.dto.response.UserInfoDto;
import lombok.NonNull;

import java.util.Optional;

public interface UserService {
    User getUserByLogin(String login);

    JwtUser getJwtUserByLogin(String login);

    UserInfoDto getUserInfo(JwtUser jwtUser);
}
