package com.example.bdsbe.dtos.request;

import com.example.bdsbe.entities.posts.*;
import com.example.bdsbe.entities.value.File;
import com.example.bdsbe.enums.Demand;
import com.example.bdsbe.enums.PostStatus;
import com.example.bdsbe.enums.Unit;
import java.util.List;
import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {

  @NotBlank(message = "Tên liên lạc không được để trống")
  private String contactName;

  @NotBlank(message = "Email không được để trống")
  private String email;

  @NotBlank(message = "Số điện thoại không được để trống")
  private String phoneNumber;

  private Demand demand;

  private Integer numberOfBedrooms;

  private Integer numberOfBathrooms;

  private Double arena;

  private Double price;

  private Unit unit;

  @NotBlank(message = "Tỉnh /Thành phố không được để trống")
  private String provinceCode;

  @NotBlank(message = "Quận / Huyện không được để trống")
  private String districtCode;

  @NotBlank(message = "Phường / Xã không được để trống")
  private String wardCode;

  @NotBlank(message = "Đường không được để trống")
  private String street;

  private Double entrance;

  private Double front;

  @Column(name = "title")
  private String title;

  @NotBlank(message = "Mô tả không được để trống")
  private String description;

  @NotEmpty(message = "Ảnh không được để trống")
  @Size(min = 3, message = "Ảnh ít nhất 3 ảnh")
  private List<File> images;

  private String videoUrl;

  private String linkMap;

  private PostStatus status;

  @NotNull(message = "Loại bất động sản không được để trống")
  private Long residentialPropertyId;

  private Long propertyLegalDocumentId;

  private Long interiorId;

  @NotNull(message = "Hướng nhà không được để trống")
  private Long houseDirectionId;

  @NotNull(message = "Hướng ban công không được để trống")
  private Long balconyDirectionId;

  private PackageBuyRequest packageBuy;
}
