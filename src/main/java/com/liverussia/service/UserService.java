package com.liverussia.service;

import com.liverussia.dao.entity.User;
import com.liverussia.domain.JwtUser;
import lombok.NonNull;

import java.util.Optional;

public interface UserService {
    User getUserByLogin(String login);

    JwtUser getJwtUserByLogin(String login);
}
