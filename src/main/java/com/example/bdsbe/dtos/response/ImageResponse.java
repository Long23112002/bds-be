package com.example.bdsbe.dtos.response;

import com.example.bdsbe.entities.value.File;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponse {

  private List<File> file;
  private Long objectId;
  private String objectName;
  private Boolean deleted;
}
