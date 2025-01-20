package com.example.bdsbe.entities.favorites;

import com.example.bdsbe.entities.primaryKey.RelFavoriteUserId;
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
@Table(name = "rel_favorite_user", schema = "favorites")
@Builder
public class RelFavoriteUser {

  @EmbeddedId private RelFavoriteUserId id;

  @Column(name = "created_at")
  @CreationTimestamp
  private Timestamp createdAt;
}
