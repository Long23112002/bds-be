package com.example.bdsbe.entities.posts;

import com.example.bdsbe.enums.TimeUnit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.sql.Timestamp;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Entity
@Table(name = "package_price", schema = "posts")
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PackagePrice {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "package_id", nullable = false)
  @NotFound(action = NotFoundAction.IGNORE)
  @JsonIgnoreProperties("packagePriceList")
  private Package aPackage;

  @Column(nullable = false, precision = 10, scale = 2)
  private Double price;

  @Column(nullable = false, length = 50)
  private TimeUnit unit;

  @Column(name = "validity")
  private Long validity;

  @Column(name = "is_enable")
  private Boolean isEnable = true;

  @Column(name = "is_deleted")
  private Boolean isDeleted = false;

  @Column(name = "created_at", updatable = false)
  @CreationTimestamp
  private Timestamp createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private Timestamp updatedAt;
}
