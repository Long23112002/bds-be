package com.example.bdsbe.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {

  private Long id;

  private String title;

  private String description;

  private String type;

  private Boolean isRead;

  private Long createdAt;
}
