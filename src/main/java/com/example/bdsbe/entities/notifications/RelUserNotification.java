package com.example.bdsbe.entities.notifications;

import com.example.bdsbe.entities.primaryKey.RelUserNotificationId;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rel_user_notifications", schema = "notifications")
@Builder
public class RelUserNotification {

  @EmbeddedId private RelUserNotificationId id;

  @Column(name = "is_read")
  private Boolean isRead;

  @CreationTimestamp
  @Column(name = "created_at")
  private Timestamp createdAt;
}
