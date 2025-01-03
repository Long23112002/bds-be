package com.example.bdsbe.entities.posts;

import java.sql.Timestamp;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "house_direction", schema = "posts")
@Where(clause = "is_deleted = false")
public class HouseDirection {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "created_at", updatable = false)
  @CreationTimestamp
  private Timestamp createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private Timestamp updatedAt;

  @Column(name = "is_deleted")
  private Boolean isDeleted = false;
}
