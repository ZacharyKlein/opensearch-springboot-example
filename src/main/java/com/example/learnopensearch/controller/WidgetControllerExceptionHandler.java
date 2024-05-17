package com.example.learnopensearch.controller;

import com.example.learnopensearch.service.DuplicateSerialNumberException;
import com.example.learnopensearch.service.WidgetNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WidgetControllerExceptionHandler {

    @ExceptionHandler(value = {WidgetNotFoundException.class, DuplicateSerialNumberException.class})
    public ResponseEntity<Body> doHandleBookExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Body(ex.getMessage()));
    }

    public static class Body {

        private String message;

        public Body(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}