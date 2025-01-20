package com.example.bdsbe.dtos.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationParam {

  private String notificationType;

  private Boolean isRead;

  private Long userId;
}
