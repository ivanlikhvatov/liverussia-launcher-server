package com.liverussia.security.utils;

import com.liverussia.dao.entity.Role;
import com.liverussia.domain.JwtUser;
import com.liverussia.security.JwtUserDetailsService;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

//    public static Authentication generate(Claims claims) {
//
//        JwtUser jwtInfoToken = new JwtUser();
//        jwtInfoToken.setRoles(getRoles(claims));
//        jwtInfoToken.setLogin(claims.getSubject());
//
//        return new UsernamePasswordAuthenticationToken(
//                userDetails,
//                CREDENTIALS,
//                userDetails.getAuthorities()
//        );
//    }

    private static Set<Role> getRoles(Claims claims) {
        List<String> roles = claims.get("roles", List.class);

        return roles.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }
}
