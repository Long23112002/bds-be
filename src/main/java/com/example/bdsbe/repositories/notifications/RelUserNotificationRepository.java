package com.example.bdsbe.repositories.notifications;

import com.example.bdsbe.entities.notifications.RelUserNotification;
import com.example.bdsbe.entities.primaryKey.RelUserNotificationId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelUserNotificationRepository
    extends JpaRepository<RelUserNotification, RelUserNotificationId> {

  List<RelUserNotification> findAllById_UserId(Long userId);
}
