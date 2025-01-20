package com.example.bdsbe.entities.posts;

import com.example.bdsbe.entities.publics.District;
import com.example.bdsbe.entities.publics.Province;
import com.example.bdsbe.entities.publics.Ward;
import com.example.bdsbe.entities.users.User;
import com.example.bdsbe.entities.value.File;
import com.example.bdsbe.enums.Demand;
import com.example.bdsbe.enums.PostStatus;
import com.example.bdsbe.enums.Unit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "post", schema = "posts")
@Where(clause = "is_deleted = false")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
public class Post implements Cloneable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "contact_name")
  private String contactName;

  @Column(name = "email")
  private String email;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "demand")
  @Enumerated(EnumType.STRING)
  private Demand demand;

  @Column(name = "number_of_bedrooms")
  private Integer numberOfBedrooms = 0;

  @Column(name = "number_of_bathrooms")
  private Integer numberOfBathrooms = 0;

  @Column(name = "arena")
  private Double arena = 0.0;

  @Column(name = "price")
  private Double price = 0.0;

  @Column(name = "unit")
  @Enumerated(EnumType.STRING)
  private Unit unit;

  @Column(name = "title")
  private String title;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "image_url", columnDefinition = "jsonb")
  @Type(type = "jsonb")
  private List<File> images;

  @Column(name = "video_url")
  private String videoUrl;

  @Column(name = "link_map", columnDefinition = "TEXT")
  private String linkMap;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "province_code")
  @NotFound(action = NotFoundAction.IGNORE)
  private Province province;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "district_code")
  @NotFound(action = NotFoundAction.IGNORE)
  private District district;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ward_code")
  @NotFound(action = NotFoundAction.IGNORE)
  private Ward ward;

  @Column(name = "street")
  private String street;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private PostStatus status;

  @Column(name = "created_at", updatable = false)
  @CreationTimestamp
  private Timestamp createdAt;

  @Column(name = "update_at")
  @UpdateTimestamp
  private Timestamp updatedAt;

  @Column(name = "is_deleted")
  private Boolean isDeleted = false;

  @Column(name = "entrance")
  private Double entrance = 0.0;

  @Column(name = "front")
  private Double front = 0.0;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  @NotFound(action = NotFoundAction.IGNORE)
  @JsonIgnoreProperties({"roles", "posts"})
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "residential_property_id")
  @NotFound(action = NotFoundAction.IGNORE)
  private ResidentialProperty residentialProperty;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "property_legal_document_id")
  @NotFound(action = NotFoundAction.IGNORE)
  private PropertyLegalDocument propertyLegalDocument;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "interior_id")
  @NotFound(action = NotFoundAction.IGNORE)
  private Interior interior;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "house_direction_id")
  @NotFound(action = NotFoundAction.IGNORE)
  private HouseDirection houseDirection;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "balcony_direction_id")
  @NotFound(action = NotFoundAction.IGNORE)
  private BalconyDirection balconyDirection;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "package_transaction_id")
  private PackagePriceTransaction packagePriceTransaction;

  @Column(name = "code_plant")
  private String codePlant;

  @Override
  public Post clone() {
    try {
      Post clone = (Post) super.clone();
      return clone;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
