package com.example.bdsbe.entities.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "user", schema = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "is_deleted = false")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, name = "phone_number")
  private String phoneNumber;

  @Column(nullable = false, name = "email")
  private String email;

  @Column(name = "password")
  private String password;

  @Column(name = "full_name")
  private String fullName;

  @Column(name = "avatar")
  private String avatar;

  @Column(name = "code")
  private String code;

  @Column(name = "is_block")
  private Boolean isBlocked = false;

  @Column(name = "is_admin")
  private Boolean isAdmin = false;

  @Column(name = "is_deleted")
  private Boolean isDeleted = false;

  @Column(name = "wallet")
  private Double wallet = 0.0;

  @CreationTimestamp
  @Column(name = "create_at")
  private Timestamp createdAt;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"),
      schema = "users")
  private List<Role> roles;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Set<GrantedAuthority> authorities =
        roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toSet());

    if (Boolean.TRUE.equals(this.isAdmin)) {
      authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
    return authorities;
  }

  @Override
  public String getUsername() {
    return phoneNumber;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !isBlocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return !isDeleted;
  }
}
