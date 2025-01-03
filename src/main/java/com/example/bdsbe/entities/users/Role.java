package com.example.bdsbe.entities.users;

import java.sql.Timestamp;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "role", schema = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "is_deleted = false")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "code")
  private String code;

  @Column(name = "created_at")
  private Timestamp createdAt;

  private Boolean isDeleted = false;

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
  private List<User> users;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "role_permission",
      joinColumns = @JoinColumn(name = "role_id"),
      inverseJoinColumns = @JoinColumn(name = "permission_id"),
      schema = "users")
  private List<Permission> permissions;
}
