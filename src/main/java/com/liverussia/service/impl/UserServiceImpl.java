package com.liverussia.service.impl;

import com.liverussia.dao.entity.user.User;
import com.liverussia.domain.JwtUser;
import com.liverussia.dto.response.SpinRouletteResponseDto;
import com.liverussia.dto.response.UserInfoDto;
import com.liverussia.error.apiException.ApiException;
import com.liverussia.error.apiException.ErrorContainer;
import com.liverussia.mapper.JwtUserMapper;
import com.liverussia.mapper.UserInfoDtoMapper;
import com.liverussia.dao.repository.UserRepository;
import com.liverussia.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUserMapper jwtUserMapper;
    private final UserInfoDtoMapper userInfoDtoMapper;

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

    @Override
    public UserInfoDto getUserInfo(JwtUser jwtUser) {

        Optional.ofNullable(jwtUser)
                .orElseThrow(() -> new ApiException(ErrorContainer.AUTHENTICATION_ERROR));

        User user = userRepository.findByLogin(jwtUser.getLogin());

        return userInfoDtoMapper.map(user);
    }


}
