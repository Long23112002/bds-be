package com.example.bdsbe.controllers;

import com.example.bdsbe.dtos.filters.PropertiesFilter;
import com.example.bdsbe.dtos.request.PropertiesRequest;
import com.example.bdsbe.entities.posts.Interior;
import com.example.bdsbe.services.posts.InteriorService;
import com.example.bdsbe.utils.ResponsePage;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/interiors")
public class InteriorController {

  @Autowired private InteriorService interiorService;

  @GetMapping
  public ResponsePage<Interior> filter(PropertiesFilter filter, Pageable pageable) {
    return new ResponsePage<>(interiorService.filter(filter, pageable));
  }

  @PostMapping
  public Interior create(@Valid @RequestBody PropertiesRequest request) {
    return interiorService.create(request);
  }

  @PutMapping("/{id}")
  public Interior update(@PathVariable Long id, @Valid @RequestBody PropertiesRequest request) {
    return interiorService.update(id, request);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    interiorService.delete(id);
  }

  @GetMapping("/{id}")
  public Interior getById(@PathVariable Long id) {
    return interiorService.getById(id);
  }
}
