package dev.plotnikov.polystore.controllers;

import dev.plotnikov.polystore.controllers.exceptions.ExceptionResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice(basePackageClasses = ExceptionController.class)
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setReason(e.getMessage());
        return ResponseEntity.internalServerError().body(exceptionResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setReason(e.getMessage());
        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> dataIntegrityViolationException(DataIntegrityViolationException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setReason("Дублирующееся значение ключа нарушает уникальное ограничение");
        return ResponseEntity.internalServerError().body(exceptionResponse);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setReason(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

}
