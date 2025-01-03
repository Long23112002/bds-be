package com.example.bdsbe.controllers;

import com.example.bdsbe.dtos.filters.PropertiesFilter;
import com.example.bdsbe.dtos.request.PropertiesRequest;
import com.example.bdsbe.entities.posts.PropertyLegalDocument;
import com.example.bdsbe.services.posts.PropertyLegalDocumentService;
import com.example.bdsbe.utils.ResponsePage;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/property_legal_documents")
public class PropertyLegalDocumentController {

  @Autowired private PropertyLegalDocumentService propertyLegalDocumentService;

  @GetMapping
  public ResponsePage<PropertyLegalDocument> filter(PropertiesFilter filter, Pageable pageable) {
    return new ResponsePage<>(propertyLegalDocumentService.filter(filter, pageable));
  }

  @PostMapping
  public PropertyLegalDocument create(@Valid @RequestBody PropertiesRequest request) {
    return propertyLegalDocumentService.create(request);
  }

  @PutMapping("/{id}")
  public PropertyLegalDocument update(
      @PathVariable Long id, @Valid @RequestBody PropertiesRequest request) {
    return propertyLegalDocumentService.update(id, request);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    propertyLegalDocumentService.delete(id);
  }

  @GetMapping("/{id}")
  public PropertyLegalDocument getById(@PathVariable Long id) {
    return propertyLegalDocumentService.getById(id);
  }
}
