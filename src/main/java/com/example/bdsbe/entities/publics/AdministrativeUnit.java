package com.example.bdsbe.entities.publics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "administrative_units", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdministrativeUnit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "full_name")
  private String fullName;

  @Column(name = "full_name_en")
  private String fullNameEn;

  @Column(name = "short_name")
  private String shortName;

  @Column(name = "short_name_en")
  private String shortNameEn;

  @Column(name = "code_name")
  private String codeName;

  @Column(name = "code_name_en")
  private String codeNameEn;

  @OneToMany(mappedBy = "administrativeUnit")
  @JsonIgnore
  private List<Province> provinces;

  @OneToMany(mappedBy = "administrativeUnit")
  @JsonIgnore
  private List<District> districts;

  @OneToMany(mappedBy = "administrativeUnit")
  @JsonIgnore
  private List<Ward> wards;
}
