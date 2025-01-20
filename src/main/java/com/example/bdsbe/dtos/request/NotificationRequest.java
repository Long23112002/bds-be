package com.example.bdsbe.dtos.request;

import com.example.bdsbe.enums.NotificationType;
import java.util.List;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {

  @NotBlank private String title;

  @NotBlank private String description;

  @NotNull private NotificationType type;

  private List<Long> userIds;

  @AssertTrue(message = "Vui lòng chọn người dùng cần thông báo")
  public boolean isUserIdValid() {
    if (type == NotificationType.POST || type == NotificationType.CUSTOMER) {
      return userIds != null && !userIds.isEmpty();
    }
    return true;
  }
}
