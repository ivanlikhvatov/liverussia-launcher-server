package com.liverussia.service;

import com.liverussia.dao.entity.User;
import lombok.NonNull;

import java.util.Optional;

public interface UserService {
    Optional<User> getByLogin(@NonNull String login);
}
