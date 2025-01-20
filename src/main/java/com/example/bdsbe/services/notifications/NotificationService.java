package com.example.bdsbe.services.notifications;

import com.example.bdsbe.dtos.filters.NotificationParam;
import com.example.bdsbe.dtos.request.NotificationRequest;
import com.example.bdsbe.dtos.response.JwtResponse;
import com.example.bdsbe.dtos.response.NotificationResponse;
import com.example.bdsbe.entities.notifications.Notification;
import com.example.bdsbe.entities.notifications.RelUserNotification;
import com.example.bdsbe.entities.primaryKey.RelUserNotificationId;
import com.example.bdsbe.entities.users.User;
import com.example.bdsbe.enums.NotificationType;
import com.example.bdsbe.repositories.notifications.NotificationRepository;
import com.example.bdsbe.repositories.notifications.RelUserNotificationRepository;
import com.example.bdsbe.repositories.users.UserRepository;
import com.example.bdsbe.services.users.AuthenticationService;
import com.example.bdsbe.services.users.JwtService;
import com.longnh.exceptions.ExceptionHandle;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

  @Autowired private RelUserNotificationRepository relUserNotificationRepository;

  @Autowired private AuthenticationService authenticationService;

  @Autowired private UserRepository userRepository;

  @Autowired private NotificationRepository notificationRepository;

  @Autowired private EntityManager entityManager;

  @Autowired private JwtService jwtService;

  @Transactional
  public void sendNotification(NotificationRequest request) {
    Notification notification = new Notification();
    notification.setTitle(request.getTitle());
    notification.setDescription(request.getDescription());
    notification.setType(request.getType());

    if (Objects.equals(request.getType(), NotificationType.GLOBAL)) {
      notificationRepository.save(notification);
      sendNotificationToAllUser(notification);

    } else {
      notificationRepository.save(notification);
      sendNotificationToUser(notification, request.getUserIds());
    }
  }

  @Transactional
  public void updateNotification(String token) {
    JwtResponse jwtResponse = jwtService.decodeToken(token);
    List<RelUserNotification> list =
        relUserNotificationRepository.findAllById_UserId(jwtResponse.getUserId());
    for (RelUserNotification relUserNotification : list) {
      relUserNotification.setIsRead(true);
      relUserNotificationRepository.save(relUserNotification);
    }
  }

  @Transactional
  public void updateNotificationById(String token, Long notificationId) {
    JwtResponse jwtResponse = jwtService.decodeToken(token);
    RelUserNotificationId id = new RelUserNotificationId(jwtResponse.getUserId(), notificationId);
    RelUserNotification relUserNotification =
        relUserNotificationRepository
            .findById(id)
            .orElseThrow(() -> new ExceptionHandle(HttpStatus.NOT_FOUND, "Notification not found"));
    relUserNotification.setIsRead(true);
    relUserNotificationRepository.save(relUserNotification);
  }

  @Transactional
  public void deleteNotificationById(String token, Long notificationId) {
    JwtResponse jwtResponse = jwtService.decodeToken(token);
    RelUserNotificationId id = new RelUserNotificationId(jwtResponse.getUserId(), notificationId);
    RelUserNotification relUserNotification =
        relUserNotificationRepository
            .findById(id)
            .orElseThrow(() -> new ExceptionHandle(HttpStatus.NOT_FOUND, "Notification not found"));
    relUserNotificationRepository.delete(relUserNotification);
  }

  public List<NotificationResponse> filterNoti(NotificationParam param) {

    List<Object[]> res =
        filterNotifications(
            param.getUserId(), String.valueOf(param.getNotificationType()), param.getIsRead());

    return mapToNotificationResponse(res);
  }

  public List<NotificationResponse> mapToNotificationResponse(List<Object[]> results) {
    return results.stream()
        .map(
            record ->
                new NotificationResponse(
                    record[0] != null ? Long.valueOf(record[0].toString()) : null,
                    record[1] != null ? record[1].toString() : null,
                    record[2] != null ? record[2].toString() : null,
                    record[3] != null ? record[3].toString() : null,
                    record[4] != null ? Boolean.valueOf(record[4].toString()) : null,
                    record[5] != null ? ((Timestamp) record[5]).getTime() : null))
        .collect(Collectors.toList());
  }

  private void sendNotificationToUser(Notification notification, List<Long> userIds) {
    for (int i = 0; i < userIds.size(); i += 100) {
      int end = Math.min(i + 100, userIds.size());
      List<Long> batch = userIds.subList(i, end);
      sendBatchNotification(notification, batch);
    }
  }

  private void sendNotificationToAllUser(Notification notification) {
    Pageable pageable = PageRequest.of(0, 100);
    Page<User> users;

    do {
      users = userRepository.findAll(pageable);
      for (User user : users) {
        RelUserNotification relUserNotification = new RelUserNotification();
        relUserNotification.setId(new RelUserNotificationId(user.getId(), notification.getId()));
        relUserNotification.setIsRead(false);
        relUserNotificationRepository.save(relUserNotification);
      }
      pageable = pageable.next();
    } while (users.hasNext());
  }

  private void sendBatchNotification(Notification notification, List<Long> batchUserIds) {
    for (Long id : batchUserIds) {
      User user = authenticationService.getUserById(id);
      if (user != null) {
        RelUserNotification relUserNotification = new RelUserNotification();
        relUserNotification.setId(new RelUserNotificationId(user.getId(), notification.getId()));
        relUserNotification.setIsRead(false);
        relUserNotificationRepository.save(relUserNotification);
      }
    }
  }

  public List<Object[]> filterNotifications(Long userId, String type, Boolean isRead) {
    StringBuilder queryStr =
        new StringBuilder(
            "SELECT n.id, n.title, n.description, n.type, rel.is_read, rel.created_at "
                + "FROM notifications.notification n "
                + "JOIN notifications.rel_user_notifications rel ON n.id = rel.notification_id "
                + "WHERE 1=1");
    if (userId != null) {
      queryStr.append(" AND rel.user_id = :userId");
    }

    if (type != null && !type.equals("null")) {
      queryStr.append(" AND n.type = :type");
    }

    if (isRead != null) {
      queryStr.append(" AND rel.is_read = :isRead");
    }

    queryStr.append(" ORDER BY n.id DESC");

    Query query = entityManager.createNativeQuery(queryStr.toString());

    if (userId != null) {
      query.setParameter("userId", userId);
    }

    if (type != null && !type.equals("null")) {
      query.setParameter("type", type);
    }

    // Thiết lập tham số `isRead` nếu có giá trị
    if (isRead != null) {
      query.setParameter("isRead", isRead);
    }

    // Log ra câu truy vấn để kiểm tra
    System.out.println("Generated Query: " + queryStr.toString());

    return query.getResultList();
  }
}
