package com.example.bdsbe.services.posts;

import com.example.bdsbe.dtos.filters.PropertiesFilter;
import com.example.bdsbe.dtos.request.PropertiesRequest;
import com.example.bdsbe.entities.posts.PropertyLegalDocument;
import com.example.bdsbe.repositories.posts.PropertyLegalDocumentRepository;
import com.longnh.exceptions.ExceptionHandle;
import com.longnh.utils.FnCommon;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PropertyLegalDocumentService {

  @Autowired private PropertyLegalDocumentRepository propertyLegalDocumentRepository;

  @Transactional
  public PropertyLegalDocument create(PropertiesRequest request) {
    PropertyLegalDocument propertyLegalDocument = new PropertyLegalDocument();
    FnCommon.copyNonNullProperties(propertyLegalDocument, request);
    return propertyLegalDocumentRepository.save(propertyLegalDocument);
  }

  public PropertyLegalDocument getById(Long id) {
    return propertyLegalDocumentRepository
        .findById(id)
        .orElseThrow(
            () -> new ExceptionHandle(HttpStatus.NOT_FOUND, "Không tìm thấy giấy tờ pháp lý"));
  }

  @Transactional
  public PropertyLegalDocument update(Long id, PropertiesRequest request) {
    PropertyLegalDocument propertyLegalDocument = getById(id);
    FnCommon.copyNonNullProperties(propertyLegalDocument, request);
    return propertyLegalDocumentRepository.save(propertyLegalDocument);
  }

  @Transactional
  public void delete(Long id) {
    PropertyLegalDocument propertyLegalDocument = getById(id);
    propertyLegalDocument.setIsDeleted(true);
    propertyLegalDocumentRepository.save(propertyLegalDocument);
  }

  public Page<PropertyLegalDocument> filter(PropertiesFilter request, Pageable pageable) {
    return propertyLegalDocumentRepository.filter(request, pageable);
  }
}
