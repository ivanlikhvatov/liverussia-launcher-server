package com.liverussia.service.impl;

import com.liverussia.dao.entity.Role;
import com.liverussia.dao.entity.User;
import com.liverussia.error.apiException.ApiException;
import com.liverussia.error.apiException.ErrorContainer;
import com.liverussia.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final List<User> users;

    public UserServiceImpl() {

        //pass 1234

        User admin = new User();
        admin.setId("1");
        admin.setLogin("admin");
        admin.setRoles(Set.of(Role.ADMIN));
        admin.setFirstName("Ivan");
        admin.setPassword("$2a$10$K5q7my0TKpc.bWYRKuZnEuCshELgxZ3cOJ16xIoXEbTDW.wt4BZfe");

        User androidUser = new User();
        androidUser.setFirstName("Gena");
        androidUser.setId("2");
        androidUser.setLogin("android-user");
        androidUser.setRoles(Set.of(Role.ANDROID_USER));
        androidUser.setPassword("9F4830BABDFC5C9185F423457FA489F9BFF4799F719775B2F8552BB4498925BA");
        androidUser.setSalt("urq+~zcS]|Z:#kVv");

        this.users = List.of(
                admin,
                androidUser
        );
    }

    @Override
    public User getByLogin(@NonNull String login) {
        return users.stream()
                .filter(user -> login.equals(user.getLogin()))
                .findFirst()
                .orElseThrow(() -> new ApiException(ErrorContainer.USER_NOT_FOUND));
    }
}
