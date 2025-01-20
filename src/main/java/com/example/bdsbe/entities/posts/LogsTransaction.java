package com.example.bdsbe.entities.posts;

import com.example.bdsbe.enums.PostStatus;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity
@Table(name = "logs_transaction", schema = "posts")
@AllArgsConstructor
@NoArgsConstructor
public class LogsTransaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "package_price_transaction", nullable = false)
  private PackagePriceTransaction packagePriceTransaction;

  @Column(name = "userapproved", nullable = false)
  private Long userApproved;

  @Column(nullable = false, precision = 10, scale = 2)
  private Double price;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private PostStatus status;

  @Column(name = "old_status")
  @Enumerated(EnumType.STRING)
  private PostStatus oldStatus;

  @Column(name = "is_deleted")
  private Boolean isDeleted = false;

  @Column(name = "created_at", updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;
}
