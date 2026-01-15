package org.yummyfood.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
public class HttpErrorInfo {
    private final ZonedDateTime timestamp = ZonedDateTime.now();
    private final HttpStatus status;
    private final String message;
    private final String path;

    public HttpErrorInfo(HttpStatus status, String message, String path) {
        this.status = status;
        this.message = message;
        this.path = path;
    }
}
