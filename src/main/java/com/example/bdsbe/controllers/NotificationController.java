package com.example.bdsbe.controllers;

import com.example.bdsbe.dtos.filters.NotificationParam;
import com.example.bdsbe.dtos.request.NotificationRequest;
import com.example.bdsbe.dtos.response.NotificationResponse;
import com.example.bdsbe.services.notifications.NotificationService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

  @Autowired private NotificationService notificationService;

  @PostMapping
  public void sendNotification(@Valid @RequestBody NotificationRequest request) {
    notificationService.sendNotification(request);
  }

  @GetMapping
  public List<NotificationResponse> filter(NotificationParam param) {
    return notificationService.filterNoti(param);
  }

  @PutMapping("/bluk")
  public void updateNotification(@RequestHeader("Authorization") String token) {
    notificationService.updateNotification(token);
  }

  @PutMapping("/{id}")
  public void updateNotification(
      @PathVariable Long id, @RequestHeader("Authorization") String token) {
    notificationService.updateNotificationById(token, id);
  }

  @DeleteMapping("/{id}")
  public void deleteNotification(
      @PathVariable Long id, @RequestHeader("Authorization") String token) {
    notificationService.deleteNotificationById(token, id);
  }
}
