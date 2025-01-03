package com.example.bdsbe.exceptions;

import com.longnh.exceptions.BaseErrorMessage;

public enum ErrorMessage implements BaseErrorMessage {
  TOKEN_INVALID("Token is invalid"),
  USER_NOT_FOUND("Không tìm thấy người dùng"),
  ACCESS_DENIED("Không có quyền truy cập"),
  JWT_EXPIRED("Token đã hết hạn"),
  ACCESS_TOKEN_NOT_FOUND("Không tìm thấy access token"),
  INVALID_REFRESH_TOKEN("Refresh token không hợp lệ"),
  BAD_CREDENTIAL("Số điện thoại hoặc mật khẩu không đúng"),
  REFRESH_TOKEN_NOT_FOUND("Không tìm thấy refresh token"),
  FILE_NOT_FOUND("Không tìm thấy file");

  public String val;

  private ErrorMessage(String label) {
    val = label;
  }

  @Override
  public String val() {
    return val;
  }
}
