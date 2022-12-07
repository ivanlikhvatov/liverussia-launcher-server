package com.liverussia.error.handler.rest;

import com.liverussia.error.apiException.ApiException;
import com.liverussia.error.apiException.ErrorContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Slf4j
@Component
public class ResourceServiceErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return !HttpStatus.OK.equals(response.getStatusCode());
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        log.debug("Не удалось загрузить данные с хранилища: {}", response.getBody());
        throw new ApiException(ErrorContainer.NETWORK_ERROR);
    }
}
