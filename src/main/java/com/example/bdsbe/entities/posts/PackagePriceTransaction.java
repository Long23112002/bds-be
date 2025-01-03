package com.example.bdsbe.entities.posts;

import com.example.bdsbe.enums.PostStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.TypeDef;

@Getter
@Setter
@Entity
@Table(name = "package_price_transaction", schema = "posts")
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PackagePriceTransaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "post_id", nullable = false)
  private Long postId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "package_price_id", nullable = false)
  private PackagePrice packagePrice;

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDate endDate;

  @Column(name = "start_time", nullable = false)
  private LocalTime startTime;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private PostStatus status;

  @Column(name = "is_deleted")
  private Boolean isDeleted = false;
}
