package com.example.bdsbe.entities.posts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Entity
@Table(name = "package", schema = "posts")
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Package {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(name = "created_at", updatable = false)
  @CreationTimestamp
  private Timestamp createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private Timestamp updatedAt;

  @Column(name = "code")
  private String code;

  @Column(nullable = false)
  private Integer level;

  @Column(name = "description")
  private String description;

  @Column(name = "is_deleted")
  private Boolean isDeleted = false;

  @OneToMany(mappedBy = "aPackage", fetch = FetchType.LAZY)
  @JsonIgnoreProperties("aPackage")
  private List<PackagePrice> packagePriceList;
}
