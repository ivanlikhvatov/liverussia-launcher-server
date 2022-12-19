package com.liverussia.config;

import com.liverussia.domain.Role;
import com.liverussia.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Value("${api.endpoints.permitAll}")
    private String[] permitAll;

    @Value("${api.endpoints.roleAdmin}")
    private String[] roleAdmin;

    @Value("${api.endpoints.roleAndroidUser}")
    private String[] roleAndroidUser;

    private final JwtFilter jwtFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic().disable()
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .authorizeHttpRequests(
                        auth -> auth
                                .antMatchers(permitAll)
                                .permitAll()
                                .antMatchers(roleAdmin)
                                .hasAuthority(Role.ADMIN_LEVEL_10.name())
                                .antMatchers(roleAndroidUser)
                                .hasAnyAuthority(
                                        Role.ANDROID_USER.name(),
                                        Role.ADMIN_LEVEL_10.name(),
                                        Role.ADMIN_LEVEL_9.name(),
                                        Role.ADMIN_LEVEL_8.name(),
                                        Role.ADMIN_LEVEL_7.name(),
                                        Role.ADMIN_LEVEL_6.name(),
                                        Role.ADMIN_LEVEL_5.name(),
                                        Role.ADMIN_LEVEL_4.name(),
                                        Role.ADMIN_LEVEL_3.name(),
                                        Role.ADMIN_LEVEL_3.name(),
                                        Role.ADMIN_LEVEL_2.name(),
                                        Role.ADMIN_LEVEL_1.name()
                                )
                                .anyRequest()
                                .authenticated()
                                .and()
                                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                ).build();
    }
}

