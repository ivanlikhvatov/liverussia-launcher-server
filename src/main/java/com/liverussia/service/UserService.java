package com.liverussia.service;

import com.liverussia.dao.entity.user.User;
import com.liverussia.domain.JwtUser;
import com.liverussia.dto.response.SpinRouletteResponseDto;
import com.liverussia.dto.response.UserInfoDto;

public interface UserService {
    User getUserByLogin(String login);

    JwtUser getJwtUserByLogin(String login);

    UserInfoDto getUserInfo(JwtUser jwtUser);

    void saveUser(User user);
}
