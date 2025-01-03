package com.example.bdsbe.controllers;

import com.example.bdsbe.dtos.filters.PropertiesFilter;
import com.example.bdsbe.dtos.request.PropertiesRequest;
import com.example.bdsbe.entities.posts.ResidentialProperty;
import com.example.bdsbe.services.posts.ResidentialService;
import com.example.bdsbe.utils.ResponsePage;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/residentials")
public class ResidentialController {

  @Autowired private ResidentialService residentialService;

  @GetMapping
  public ResponsePage<ResidentialProperty> filter(PropertiesFilter filter, Pageable pageable) {
    return new ResponsePage<>(residentialService.filter(filter, pageable));
  }

  @PostMapping
  public ResidentialProperty create(@Valid @RequestBody PropertiesRequest request) {
    return residentialService.create(request);
  }

  @PutMapping("/{id}")
  public ResidentialProperty update(
      @PathVariable Long id, @Valid @RequestBody PropertiesRequest request) {
    return residentialService.update(id, request);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    residentialService.delete(id);
  }

  @GetMapping("/{id}")
  public ResidentialProperty getById(@PathVariable Long id) {
    return residentialService.getById(id);
  }
}
