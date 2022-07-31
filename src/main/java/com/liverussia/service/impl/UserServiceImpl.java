package com.liverussia.service.impl;

import com.liverussia.dao.entity.User;
import com.liverussia.domain.JwtUser;
import com.liverussia.error.apiException.ApiException;
import com.liverussia.error.apiException.ErrorContainer;
import com.liverussia.mapper.JwtUserMapper;
import com.liverussia.repository.UserRepository;
import com.liverussia.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUserMapper jwtUserMapper;

    @Override
    public User getUserByLogin(String login) {
        return Optional.ofNullable(userRepository.findByLogin(login))
                .orElseThrow(() -> new ApiException(ErrorContainer.USER_NOT_FOUND));
    }

    @Override
    public JwtUser getJwtUserByLogin(String login) {
        User user = getUserByLogin(login);

        Optional.ofNullable(user)
                .orElseThrow(() -> new ApiException(ErrorContainer.USER_NOT_FOUND));

        return jwtUserMapper.map(user);
    }
}
