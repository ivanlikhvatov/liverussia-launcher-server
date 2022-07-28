package com.liverussia.security;

import com.liverussia.dao.entity.User;
import com.liverussia.domain.TokenAndExpiration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Slf4j
@Component
public class JwtProvider {

    private final static String CREDENTIALS = "";
    private final static String ROLES_LIST_NAME = "roles";

    private final UserDetailsService jwtUserDetailsService;
    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;
    private final int accessTokenExpirationMinutes;
    private final int refreshTokenExpirationDays;

    public JwtProvider(
            @Value("${jwt.secret.access.token}") String jwtAccessSecret,
            @Value("${jwt.secret.refresh.token}") String jwtRefreshSecret,
            @Value("${jwt.secret.access.duration.minutes}") int accessTokenExpirationMinutes,
            @Value("${jwt.secret.refresh.duration.days}") int refreshTokenExpirationDays,
            JwtUserDetailsService jwtUserDetailsService
    ) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
        this.accessTokenExpirationMinutes = accessTokenExpirationMinutes;
        this.refreshTokenExpirationDays = refreshTokenExpirationDays;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    public String generateAccessToken(User user) {
        LocalDateTime now = LocalDateTime.now();
        Instant accessExpirationInstant = now.plusMinutes(accessTokenExpirationMinutes)
                .atZone(ZoneId.systemDefault())
                .toInstant();

        Date accessExpiration = Date.from(accessExpirationInstant);

        return Jwts.builder()
                .setSubject(user.getLogin())
                .setExpiration(accessExpiration)
                .signWith(jwtAccessSecret)
                .claim(ROLES_LIST_NAME, user.getRoles())
                .compact();
    }

    public String generateRefreshToken(User user) {
        LocalDateTime now = LocalDateTime.now();
        Instant refreshExpirationInstant = now.plusDays(refreshTokenExpirationDays)
                .atZone(ZoneId.systemDefault())
                .toInstant();

        Date refreshExpiration = Date.from(refreshExpirationInstant);

        return Jwts.builder()
                .setSubject(user.getLogin())
                .setExpiration(refreshExpiration)
                .signWith(jwtRefreshSecret)
                .compact();
    }

    public boolean validateAccessToken(String accessToken) {
        try {
            return validateToken(accessToken, jwtAccessSecret);
        } catch (AuthenticationException e){
            return false;
        }
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            return validateToken(refreshToken, jwtRefreshSecret);
        } catch (AuthenticationException e){
            return false;
        }
    }


    private boolean validateToken(String token, Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
//        } catch (ExpiredJwtException e) {
//            throw new UsernameNotFoundException(e.getMessage());
//        } catch (UnsupportedJwtException e) {
//            throw new UsernameNotFoundException(e.getMessage());
//        } catch (MalformedJwtException e) {
//            throw new UsernameNotFoundException(e.getMessage());
//        } catch (SignatureException e) {
//            throw new UsernameNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

//    private boolean validateToken(String token, Key secret) {
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(secret)
//                    .build()
//                    .parseClaimsJws(token);
//            return true;
//        } catch (ExpiredJwtException e) {
//            throw new UsernameNotFoundException(e.getMessage());
//        } catch (UnsupportedJwtException unsEx) {
//            log.error("Unsupported jwt", unsEx);
//        } catch (MalformedJwtException mjEx) {
//            log.error("Malformed jwt", mjEx);
//        } catch (SignatureException sEx) {
//            log.error("Invalid signature", sEx);
//        } catch (Exception e) {
//            log.error("invalid token", e);
//        }
//        return false;
//    }

    public Claims getAccessClaims(String token) {
        return getClaims(token, jwtAccessSecret);
    }

    public Claims getRefreshClaims(String token) {
        return getClaims(token, jwtRefreshSecret);
    }

    private Claims getClaims(String token, Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Authentication getAuthentication(String token) {

        Claims claims = getAccessClaims(token);
        String login = claims.getSubject();

        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(login);

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                CREDENTIALS,
                userDetails.getAuthorities()
        );
    }
}
