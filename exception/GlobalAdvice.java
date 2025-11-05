package edu.famu.cop3060.resources.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalAdvice {

  private Map<String,Object> payload(HttpStatus status, String message, String path) {
    Map<String,Object> m = new HashMap<>();
    m.put("timestamp", Instant.now().toString());
    m.put("status", status.value());
    m.put("error", status.getReasonPhrase());
    m.put("message", message);
    m.put("path", path);
    return m;
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<?> handleNotFound(NotFoundException ex, org.springframework.web.context.request.WebRequest req) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(payload(HttpStatus.NOT_FOUND, ex.getMessage(), req.getDescription(false)));
  }

  @ExceptionHandler(InvalidReferenceException.class)
  public ResponseEntity<?> handleInvalid(InvalidReferenceException ex, org.springframework.web.context.request.WebRequest req) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(payload(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getDescription(false)));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex, org.springframework.web.context.request.WebRequest req) {
    String msg = ex.getBindingResult().getFieldErrors().stream()
        .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
        .findFirst().orElse("validation error");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(payload(HttpStatus.BAD_REQUEST, msg, req.getDescription(false)));
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<?> handleConflict(IllegalStateException ex, org.springframework.web.context.request.WebRequest req) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(payload(HttpStatus.CONFLICT, ex.getMessage(), req.getDescription(false)));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleAny(Exception ex, org.springframework.web.context.request.WebRequest req) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(payload(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), req.getDescription(false)));
  }
}
