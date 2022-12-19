package com.liverussia.mapper;

import com.liverussia.dao.entity.user.User;
import com.liverussia.domain.JwtUser;
import com.liverussia.domain.Role;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Set;

@Mapper
public interface JwtUserMapper {

    JwtUser map(User user);

    @AfterMapping
    default void mapAfter(@MappingTarget JwtUser jwtUser, User user) {
        Set<Role> userRoles = Role.getAllRolesByAdminLevel(user.getAdminLevel());
        jwtUser.setRoles(userRoles);
//
//        String password = user.getPassword();
//        String salt = user.getSalt();
//
//        jwtUser.setPassword(password.concat(salt));
    }

}
