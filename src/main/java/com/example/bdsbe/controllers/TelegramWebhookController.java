package com.example.bdsbe.controllers;

import com.example.bdsbe.services.bots.TelegramWebhookService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/telegram")
public class TelegramWebhookController {

  @Autowired private TelegramWebhookService telegramWebhookService;

  @PostMapping("/webhook")
  public ResponseEntity<String> handleUpdate(@RequestBody Map<String, Object> update) {
    try {
      return telegramWebhookService.processUpdate(update);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body("Error processing update: " + e.getMessage());
    }
  }
}
