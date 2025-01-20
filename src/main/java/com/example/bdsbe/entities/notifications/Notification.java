package com.example.bdsbe.entities.notifications;

import com.example.bdsbe.enums.NotificationType;
import java.sql.Timestamp;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "notification", schema = "notifications")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "title")
  private String title;

  @Column(name = "description")
  private String description;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private NotificationType type;

  @CreationTimestamp
  @Column(name = "created_at")
  private Timestamp createdAt;

  @Transient private Boolean isRead;
}
