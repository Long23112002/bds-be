package com.example.bdsbe.controllers;

import com.example.bdsbe.dtos.filters.PropertiesFilter;
import com.example.bdsbe.dtos.request.PropertiesRequest;
import com.example.bdsbe.entities.posts.BalconyDirection;
import com.example.bdsbe.services.posts.BalconyDirectionService;
import com.example.bdsbe.utils.ResponsePage;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/balcony_directions")
public class BalconyDirectionController {

  @Autowired private BalconyDirectionService balconyDirectionService;

  @GetMapping
  public ResponsePage<BalconyDirection> filter(PropertiesFilter filter, Pageable pageable) {
    return new ResponsePage<>(balconyDirectionService.filter(filter, pageable));
  }

  @PostMapping
  public BalconyDirection create(@Valid @RequestBody PropertiesRequest request) {
    return balconyDirectionService.create(request);
  }

  @PutMapping("/{id}")
  public BalconyDirection update(
      @PathVariable Long id, @Valid @RequestBody PropertiesRequest request) {
    return balconyDirectionService.update(id, request);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    balconyDirectionService.delete(id);
  }

  @GetMapping("/{id}")
  public BalconyDirection getById(@PathVariable Long id) {
    return balconyDirectionService.getById(id);
  }
}
