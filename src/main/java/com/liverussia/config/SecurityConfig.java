package com.liverussia.config;

import com.liverussia.dao.entity.Role;
import com.liverussia.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Value("${api.endpoints.permitAll}")
    private String[] permitAll;

    @Value("${api.endpoints.roleAdmin}")
    private String[] roleAdmin;

    @Value("${api.endpoints.roleAndroidUser}")
    private String[] roleStudent;

    private final JwtFilter jwtFilter;

//    @Bean
//    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http
//                .httpBasic().disable()
//                .cors().and()
//                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeHttpRequests(
//                        auth -> auth
//                                .antMatchers(permitAll)
//                                .permitAll()
//                                .antMatchers(roleAdmin)
//                                .hasAuthority(Role.ADMIN.name())
//                                .antMatchers(roleStudent)
//                                .hasAuthority(Role.ANDROID_USER.name())
//                                .anyRequest()
//                                .authenticated()
//                                .and()
//                                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
//                ).build();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .httpBasic().disable()
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(permitAll)
                .permitAll()
                .antMatchers(roleAdmin)
                .hasAuthority(Role.ADMIN.name())
                .antMatchers(roleStudent)
                .hasAuthority(Role.ANDROID_USER.name())
                .anyRequest()
                .authenticated()
                .and()
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}

