package com.liverussia.security;

import com.liverussia.config.SaltPasswordEncoder;
import com.liverussia.dao.entity.User;
import com.liverussia.error.apiException.ApiException;
import com.liverussia.error.apiException.ErrorContainer;
import com.liverussia.mapper.JwtUserMapper;
import com.liverussia.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final JwtUserMapper jwtUserMapper;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userService.getByLogin(login);

        Optional.ofNullable(user)
                .orElseThrow(() -> new ApiException(ErrorContainer.USER_NOT_FOUND));

        //TODO
        SaltPasswordEncoder.SALT = user.getSalt();

        return jwtUserMapper.map(user);
    }
}
