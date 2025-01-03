package com.example.bdsbe.controllers;

import com.example.bdsbe.dtos.filters.PropertiesFilter;
import com.example.bdsbe.dtos.request.PropertiesRequest;
import com.example.bdsbe.entities.posts.HouseDirection;
import com.example.bdsbe.services.posts.HouseDirectionService;
import com.example.bdsbe.utils.ResponsePage;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/house_directions")
public class HouseDirectionController {

  @Autowired private HouseDirectionService houseDirectionService;

  @GetMapping
  public ResponsePage<HouseDirection> filter(PropertiesFilter filter, Pageable pageable) {
    return new ResponsePage<>(houseDirectionService.filter(filter, pageable));
  }

  @PostMapping
  public HouseDirection create(@Valid @RequestBody PropertiesRequest request) {
    return houseDirectionService.create(request);
  }

  @PutMapping("/{id}")
  public HouseDirection update(
      @PathVariable Long id, @Valid @RequestBody PropertiesRequest request) {
    return houseDirectionService.update(id, request);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    houseDirectionService.delete(id);
  }

  @GetMapping("/{id}")
  public HouseDirection getById(@PathVariable Long id) {
    return houseDirectionService.getById(id);
  }
}
