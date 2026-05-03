package com.example.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(IllegalArgumentException ex, HttpServletRequest req) {
        log.warn("Плохой запрос: {} {} -> {}", req.getMethod(), req.getRequestURI(), ex.getMessage());
        return error(req, 400, "Bad request", ex.getMessage(), false, ex);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleState(IllegalStateException ex, HttpServletRequest req){
        log.warn("Конфликт: {} {} -> {}", req.getMethod(), req.getRequestURI(), ex.getMessage());
        return error(req, 409, "Conflict", ex.getMessage(), false, ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleGeneric(Exception ex, HttpServletRequest req) {
        String errorId = UUID.randomUUID().toString();
        log.error("Unhandled exception id={} {} {} -> {}", errorId, req.getMethod(), req.getRequestURI(), ex.getMessage(), ex);
        return error(req, 500, "Internal Server Error", "Unexpected error. id=" + errorId, true, ex);
    }

    private ResponseEntity<Map<String, Object>> error(
            HttpServletRequest req,
            int status,
            String error,
            String message,
            boolean includePath,
            Exception ex
    ) {
        Map<String, Object> responce = new HashMap<>();
        responce.put("timestamp", Instant.now().toString());
        responce.put("status", status);
        responce.put("error", error);
        responce.put("message", message);

        responce.put("method", req.getMethod());
        if (includePath) {
            responce.put("path", req.getRequestURI());
        }

        return ResponseEntity.status(HttpStatus.valueOf(status)).body(responce);

    }

}
