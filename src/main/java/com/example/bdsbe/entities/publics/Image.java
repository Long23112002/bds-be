package com.example.bdsbe.entities.publics;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import java.sql.Timestamp;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted = false")
@Table(name = "images", schema = "images")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Image {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "url")
  private String url;

  @Column(name = "object_id")
  private Long objectId;

  @Column(name = "object_name")
  private String objectName;

  @Column(name = "created_at")
  @CreationTimestamp
  private Timestamp createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private Timestamp updatedAt;

  @Column(name = "deleted")
  private Boolean deleted = false;
}
