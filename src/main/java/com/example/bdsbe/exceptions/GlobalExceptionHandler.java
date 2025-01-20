package com.example.bdsbe.exceptions;

import com.example.bdsbe.services.bots.TelegramBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @Autowired private TelegramBotService telegramBotService;

  // Xử lý tất cả các loại exception
  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleException(Exception e) {
    // Lấy tên phương thức và thông điệp lỗi
    StackTraceElement[] stackTrace = e.getStackTrace();
    String methodName = stackTrace.length > 0 ? stackTrace[0].getMethodName() : "Unknown method";
    String errorMessage = e.getMessage();

    // Gửi thông báo lỗi qua Telegram
    telegramBotService.sendErrorMessage(methodName, errorMessage);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("An error occurred. The issue has been reported.");
  }
}
