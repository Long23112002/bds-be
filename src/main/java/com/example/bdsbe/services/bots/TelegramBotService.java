package com.example.bdsbe.services.bots;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramBotService {

  @Value("${telegram.bot-post-online.token}")
  private String token;

  @Value("${telegram.bot-post-online.chat-id}")
  private String chatId;

  @Value("${telegram.url}")
  private String urlTelegram;

  @Value("${telegram.bot-post-online.chat-id-bot-error}")
  private String chatIDError;

  @Value("${telegram.bot-post-online.token-bot-error}")
  private String tokenBotError;

  @Autowired private RestTemplate restTemplate;

  public void sendMessageWithButtons(
      String postId,
      String postTitle,
      String userName,
      String phoneNumber,
      String servicePackage,
      String startDate,
      String endDate,
      String link) {
    String url = String.format("%s%s/sendMessage", urlTelegram, token);

    // Tạo nội dung tin nhắn với định dạng HTML
    String message =
        String.format(
            "<b>Mã bài viết:</b> %s\n<b>Tên bài viết:</b> %s\n<b>Tên người đăng:</b> %s\n"
                + "<b>Số điện thoại:</b> %s\n<b>Gói dịch vụ:</b> %s\n"
                + "<b>Ngày bắt đầu:</b> %s\n<b>Ngày kết thúc:</b> %s\n"
                + "<b>Link bài viết:</b> <a href='%s'>Xem bài viết</a>",
            postId, postTitle, userName, phoneNumber, servicePackage, startDate, endDate, link);

    try {
      // Payload cho phương thức sendMessage
      Map<String, Object> payload =
          Map.of(
              "chat_id",
              chatId,
              "text",
              message,
              "parse_mode",
              "HTML",
              "reply_markup",
              Map.of(
                  "inline_keyboard",
                  new Object[][] {
                    {Map.of("text", "Duyệt", "callback_data", "approve:" + postId)},
                    {Map.of("text", "Hủy", "callback_data", "cancel:" + postId)}
                  }));

      // Gửi request
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<String> request =
          new HttpEntity<>(new ObjectMapper().writeValueAsString(payload), headers);

      ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
      System.out.println("Response: " + response.getBody());
    } catch (Exception e) {
      System.err.println("Failed to send message with buttons: " + e.getMessage());
    }
  }

  //  public void sendPhotoWithButtons(
  //      String photoUrl,
  //      String postId,
  //      String userName,
  //      String phoneNumber,
  //      String servicePackage,
  //      String startDate,
  //      String endDate) {
  //    String url = String.format("%s%s/sendPhoto", urlTelegram, token);
  //
  //    // Lấy thông tin cần thiết để tạo caption với HTML
  //    String caption =
  //        String.format(
  //            "<b>Mã bài viết:</b> %s\n<b>Tên người đăng:</b> %s\n<b>Số điện thoại:</b> %s\n"
  //                + "<b>Gói dịch vụ:</b> %s\n<b>Ngày bắt đầu:</b> %s\n<b>Ngày kết thúc:</b> %s",
  //            postId, userName, phoneNumber, servicePackage, startDate, endDate);
  //
  //    try {
  //
  //      Map<String, Object> payload =
  //          Map.of(
  //              "chat_id",
  //              chatId,
  //              //              "photo", photoUrl,
  //              "caption",
  //              caption,
  //              "parse_mode",
  //              "HTML",
  //              "reply_markup",
  //              Map.of(
  //                  "inline_keyboard",
  //                  new Object[][] {
  //                    {Map.of("text", "Duyệt", "callback_data", "approve:" + postId)},
  //                    {Map.of("text", "Hủy", "callback_data", "cancel:" + postId)}
  //                  }));
  //
  //      // Gửi request
  //      HttpHeaders headers = new HttpHeaders();
  //      headers.setContentType(MediaType.APPLICATION_JSON);
  //      HttpEntity<String> request =
  //          new HttpEntity<>(new ObjectMapper().writeValueAsString(payload), headers);
  //
  //      ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
  //      System.out.println("Response: " + response.getBody());
  //    } catch (Exception e) {
  //      System.err.println("Failed to send photo with buttons: " + e.getMessage());
  //    }
  //  }

  public void sendMessage(String message) {
    String url = String.format("%s%s/sendMessage", urlTelegram, token);

    try {
      // Tạo payload để gửi thông báo
      Map<String, Object> payload =
          Map.of(
              "chat_id", chatId,
              "text", message);

      // Gửi request
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<String> request =
          new HttpEntity<>(new ObjectMapper().writeValueAsString(payload), headers);

      ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
      System.out.println("Response: " + response.getBody());
    } catch (Exception e) {
      System.err.println("Failed to send message: " + e.getMessage());
    }
  }

  public void sendErrorMessage(String methodName, String errorMessage) {
    try {
      String timestamp =
          LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
      String message =
          String.format(
              "Error occurred in method: %s\nMessage: %s\nTimestamp: %s",
              methodName, errorMessage, timestamp);
      String url =
          urlTelegram
              + tokenBotError
              + "?chat_id="
              + chatIDError
              + "&text="
              + URLEncoder.encode(message, "UTF-8");
      System.out.println("Sending error message to Telegram with URL: " + url);
      restTemplate.getForObject(url, String.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
