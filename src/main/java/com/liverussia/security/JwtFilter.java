package com.liverussia.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final static String AUTHORIZATION_HEADER_NAME = "Authorization";
    private final static String BEARER_TOKEN_STARTS_STRING = "Bearer ";
    private final static Integer BEARER_TOKEN_STARTS_INDEX = 7;

    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String token = getTokenFromRequest((HttpServletRequest) request);

        if (token != null && jwtProvider.validateAccessToken(token)) {
            Authentication jwtInfoToken = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER_NAME);

        if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith(BEARER_TOKEN_STARTS_STRING)) {
            return bearerToken.substring(BEARER_TOKEN_STARTS_INDEX);
        }

        return null;
    }

}