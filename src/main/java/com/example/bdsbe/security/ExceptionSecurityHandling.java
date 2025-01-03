package com.example.bdsbe.security;

import com.example.bdsbe.dtos.response.ErrorResponse;
import com.example.bdsbe.exceptions.ErrorMessage;
import java.time.Instant;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@SuppressWarnings("unused")
public class ExceptionSecurityHandling {
  @ExceptionHandler({AccessDeniedException.class})
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(
      AccessDeniedException ex, HttpServletRequest request) {
    String path = request.getRequestURI();
    String timestamp = Instant.now().toString();
    ErrorResponse errorResponse =
        new ErrorResponse(
            HttpServletResponse.SC_FORBIDDEN, ErrorMessage.ACCESS_DENIED.val(), path, timestamp);

    return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler({AuthenticationException.class})
  public ResponseEntity<ErrorResponse> handleAuthenticationException(
      AuthenticationException ex, HttpServletRequest request) {
    String path = request.getRequestURI();
    String timestamp = Instant.now().toString();
    ErrorResponse errorResponse =
        new ErrorResponse(
            HttpServletResponse.SC_UNAUTHORIZED, ErrorMessage.JWT_EXPIRED.val(), path, timestamp);

    return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
  }
}
