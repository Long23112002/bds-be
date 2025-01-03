package com.example.bdsbe.controllers;

import com.example.bdsbe.dtos.filters.ImageParam;
import com.example.bdsbe.dtos.request.ImageRequest;
import com.example.bdsbe.dtos.response.ImageResponse;
import com.example.bdsbe.entities.publics.Image;
import com.example.bdsbe.services.publics.ImageUploadService;
import com.example.bdsbe.utils.ResponsePage;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

  @Autowired private ImageUploadService imageUploadService;

  @PostMapping
  public ResponseEntity<ImageResponse> uploadImage(@ModelAttribute ImageRequest dto)
      throws IOException {
    return ResponseEntity.ok(imageUploadService.uploadImage(dto));
  }

  @GetMapping
  public ResponsePage<Image> filter(ImageParam param, Pageable pageable) {
    return new ResponsePage<>(imageUploadService.filter(param, pageable));
  }
}
