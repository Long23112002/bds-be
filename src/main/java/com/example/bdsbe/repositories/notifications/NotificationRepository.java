package com.example.bdsbe.repositories.notifications;

import com.example.bdsbe.entities.notifications.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  //  @Query(
  //      "SELECT new com.example.bdsbe.dtos.response.NotificationResponse(n.id,"
  //          + "n.title, n.description, n.type,  r.isRead,r.createdAt ) "
  //          + "FROM Notification n "
  //          + "JOIN RelUserNotification r ON r.id.notificationId = n.id "
  //          + "WHERE (:#{#param.userId} IS NULL OR r.id.userId = :#{#param.userId}) "
  //          + "AND (:#{#param.notificationType} IS NULL OR n.type = :#{#param.notificationType}) "
  //          + "AND (:#{#param.isRead} IS NULL OR r.isRead = :#{#param.isRead})")
  //  List<NotificationResponse> filter(NotificationParam param);
}
