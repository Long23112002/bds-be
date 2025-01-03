package com.example.bdsbe.dtos.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

  @NotBlank(message = "Số điện thoại không được để trống")
  private String phoneNumber;

  @NotBlank(message = "Mật khẩu không được để trống")
  private String password;
}
