package com.alchin.shortlink;

import com.alchin.shortlink.link.LinkExpiredException;
import com.alchin.shortlink.link.LinkNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> badRequest(IllegalArgumentException ex) {
        return error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(LinkNotFoundException.class)
    public ResponseEntity<Map<String, Object>> notFound(LinkNotFoundException ex) {
        return error(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(LinkExpiredException.class)
    public ResponseEntity<Map<String, Object>> expired(LinkExpiredException ex) {
        return error(HttpStatus.GONE, ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of(
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message,
                "timestamp", LocalDateTime.now().toString()
        ));
    }
}
