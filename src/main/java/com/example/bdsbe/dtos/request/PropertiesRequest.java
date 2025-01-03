package com.example.bdsbe.dtos.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertiesRequest {

  @NotBlank(message = "Tên không được để trống")
  private String name;
}
