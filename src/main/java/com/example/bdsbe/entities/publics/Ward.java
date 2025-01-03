package com.example.bdsbe.entities.publics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "wards", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ward {

  @Id
  @Column(nullable = false)
  private String code;

  @Column(nullable = false)
  private String name;

  @Column(name = "name_en")
  private String nameEn;

  @Column(name = "full_name")
  private String fullName;

  @Column(name = "full_name_en")
  private String fullNameEn;

  @Column(name = "code_name")
  private String codeName;

  @ManyToOne
  @JoinColumn(name = "district_code")
  @JsonIgnore
  private District district;

  @ManyToOne
  @JsonIgnore
  @JoinColumn(name = "administrative_unit_id")
  private AdministrativeUnit administrativeUnit;
}
