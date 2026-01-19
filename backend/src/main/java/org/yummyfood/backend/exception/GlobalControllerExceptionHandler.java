package org.yummyfood.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ExceptionHandler({
            NotFoundException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<HttpErrorInfo> handleNotFoundException(Exception ex, HttpServletRequest request) {
        return buildHttpErrorInfo(HttpStatus.NOT_FOUND, request, ex);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<HttpErrorInfo> handleInvalidInputException(InvalidInputException ex, HttpServletRequest request) {
        return buildHttpErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY, request, ex);
    }

    private ResponseEntity<HttpErrorInfo> buildHttpErrorInfo(HttpStatus status, HttpServletRequest request, Exception exception) {
        final String path = request.getRequestURI();
        final String message = exception.getMessage();

        log.debug("Returning HTTP status: {} for path: {} and message: {}", status, path, message);
        return ResponseEntity.status(status).body(new HttpErrorInfo(status, message, path));
    }
}
