package com.example.bdsbe.dtos.filters;

import com.example.bdsbe.enums.Demand;
import com.example.bdsbe.enums.PostStatus;
import java.util.List;
import java.util.Set;
import lombok.Data;

@Data
public class PostParam {

  private Long userId;

  private Double minPrice;

  private Double maxPrice;

  private String provinceCode;

  private String districtCode;

  private String wardCode;

  private String street;

  private String keyword;

  private Integer maxArea;

  private Integer minArea;

  private Set<Long> residentialPropertyIds;

  private Set<Long> houseDirectionIds;

  private Set<Long> balconyDirectionIds;

  private Set<Long> numBerOfBedrooms;

  private PostStatus status;

  private String codePlant;

  private Demand demand;

  private List<String> media;

  private Boolean isExpired = false;

  public Demand getDemand() {
    return demand != null ? demand : Demand.SELL;
  }
}
