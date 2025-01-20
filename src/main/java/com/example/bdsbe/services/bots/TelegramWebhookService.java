package com.example.bdsbe.services.bots;

import com.example.bdsbe.dtos.response.MessageResponse;
import com.example.bdsbe.enums.PostStatus;
import com.example.bdsbe.services.posts.PostService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TelegramWebhookService {

  @Autowired private TelegramBotService telegramBotService;
  @Autowired private PostService postService;

  public ResponseEntity<String> processUpdate(Map<String, Object> update) {
    if (update.containsKey("callback_query")) {
      Map<String, Object> callbackQuery = (Map<String, Object>) update.get("callback_query");
      String callbackData = (String) callbackQuery.get("data");
      Map<String, Object> from = (Map<String, Object>) callbackQuery.get("from");
      String userId = from.get("id").toString();

      String[] dataParts = callbackData.split(":");
      String action = dataParts[0];
      String postId = dataParts[1];

      return handleCallbackAction(action, postId, Long.parseLong(userId));
    } else {
      return ResponseEntity.badRequest().body("No callback query found in the update");
    }
  }

  private ResponseEntity<String> handleCallbackAction(String action, String postId, Long chatId) {
    try {
      if ("approve".equals(action)) {
        MessageResponse messageResponse =
            postService.approvedPost(Long.parseLong(postId), PostStatus.APPROVED, chatId, "");
        telegramBotService.sendMessage(messageResponse.getMessage());
        return ResponseEntity.ok("Post " + postId + " approved");

      } else if ("cancel".equals(action)) {
        MessageResponse messageResponse =
            postService.approvedPost(Long.parseLong(postId), PostStatus.REJECTED, chatId, "");
        telegramBotService.sendMessage(messageResponse.getMessage());
        return ResponseEntity.ok("Post " + postId + " cancelled");
      } else {
        return ResponseEntity.badRequest().body("Unknown action: " + action);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body("Error processing action: " + e.getMessage());
    }
  }
}
