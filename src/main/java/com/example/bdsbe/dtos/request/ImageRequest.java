package com.example.bdsbe.dtos.request;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageRequest {
  @NotEmpty private List<MultipartFile> file;

  private Long objectId;

  private String objectName;
}
