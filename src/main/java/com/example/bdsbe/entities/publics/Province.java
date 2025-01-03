package com.example.bdsbe.entities.publics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "provinces", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Province {

  @Id
  @Column(nullable = false)
  private String code;

  @Column(nullable = false)
  private String name;

  @Column(name = "name_en")
  private String nameEn;

  @Column(name = "full_name", nullable = false)
  private String fullName;

  @Column(name = "full_name_en")
  private String fullNameEn;

  @Column(name = "code_name")
  private String codeName;

  @ManyToOne
  @JoinColumn(name = "administrative_unit_id")
  @JsonIgnore
  private AdministrativeUnit administrativeUnit;

  @ManyToOne
  @JoinColumn(name = "administrative_region_id")
  @JsonIgnore
  private AdministrativeRegion administrativeRegion;

  @OneToMany(mappedBy = "province")
  @JsonIgnore
  private List<District> districts;
}
