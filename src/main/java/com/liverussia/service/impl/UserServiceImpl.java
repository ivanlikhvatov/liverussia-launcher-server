package com.liverussia.service.impl;

import com.liverussia.dao.entity.Role;
import com.liverussia.dao.entity.User;
import com.liverussia.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final List<User> users;

    public UserServiceImpl() {

        User admin = new User();
        admin.setId("1");
        admin.setLogin("admin");
        admin.setRoles(Set.of(Role.ADMIN));
        admin.setFirstName("Ivan");
        admin.setPassword("1234");

        User androidUser = new User();
        androidUser.setFirstName("Gena");
        androidUser.setId("2");
        androidUser.setLogin("android-user");
        androidUser.setRoles(Set.of(Role.ANDROID_USER));
        androidUser.setPassword("1234");

        this.users = List.of(
                admin,
                androidUser
        );
    }

    @Override
    public Optional<User> getByLogin(@NonNull String login) {
        return users.stream()
                .filter(user -> login.equals(user.getLogin()))
                .findFirst();
    }
}
