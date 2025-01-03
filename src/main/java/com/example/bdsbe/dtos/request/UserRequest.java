package com.example.bdsbe.dtos.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

  @Email(message = "Email không đúng định dạng")
  @NotBlank(message = "Email không được để trống")
  private String email;

  @NotBlank(message = "Họ và tên không được để trống")
  private String fullName;

  @NotBlank(message = "Số điện thoại không được để trống")
  private String phoneNumber;

  @NotBlank(message = "Mật khẩu không được để trống")
  private String password;
}
