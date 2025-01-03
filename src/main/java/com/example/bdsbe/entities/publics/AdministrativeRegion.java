package com.example.bdsbe.entities.publics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "administrative_regions", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdministrativeRegion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @Column(name = "name_en", nullable = false)
  private String nameEn;

  @Column(name = "code_name")
  private String codeName;

  @Column(name = "code_name_en")
  private String codeNameEn;

  @OneToMany(mappedBy = "administrativeRegion")
  @JsonIgnore
  private List<Province> provinces;
}
