package com.liverussia.error.apiException;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorContainer {

    AUTHENTICATION_ERROR(210, "Authentication error", HttpStatus.UNAUTHORIZED),
    BAD_REQUEST(211, "Request validation error", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(212, "Пользователь не найден", HttpStatus.NOT_FOUND),
    USER_WITH_THIS_LOGIN_EXIST(213, "Пользователь с таким логином уже зарегистрирован", HttpStatus.BAD_REQUEST),
    PAGE_NOT_FOUND(214, "Page not found", HttpStatus.NOT_FOUND),
    ACCESS_DENIED(215, "Access Denied", HttpStatus.FORBIDDEN),
    USER_WITH_THIS_LOGIN_ALREADY_REGISTER(216, "Пользователь уже зарегистрирован", HttpStatus.BAD_REQUEST),
    USER_WITH_THIS_LOGIN_NOT_FOUND(217, "Пользователь с таким логином не найден", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(218, "Срок действия токена истек", HttpStatus.UNAUTHORIZED),

    CAPTCHA_ERROR(219, "Попробуйте заново пройти google captcha", HttpStatus.UNAUTHORIZED),
    NOT_ENOUGH_MONEY(220, "Недостаточно денег", HttpStatus.BAD_REQUEST),

    NETWORK_ERROR(221, "Не удалось загрузить данные. Попробуйте еще раз", HttpStatus.NOT_FOUND),

    OTHER(999, "Other Type of Error. See error message", HttpStatus.INTERNAL_SERVER_ERROR);

    private static final ErrorContainer[] VALUES = values();

    private final int code;

    private final String message;

    private final HttpStatus httpStatus;

    @Override
    public String toString() {
        return "ErrorContainer{" +
                "message='" + message + '\'' +
                ", code=" + code +
                '}';
    }

    public static ErrorContainer of(int errorCode) {
        return Arrays.stream(VALUES)
                .filter(errorContainer ->
                        errorContainer.getCode() == errorCode)
                .findFirst()
                .orElse(null);
    }
}
