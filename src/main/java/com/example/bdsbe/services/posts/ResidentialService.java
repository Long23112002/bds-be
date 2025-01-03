package com.example.bdsbe.services.posts;

import com.example.bdsbe.dtos.filters.PropertiesFilter;
import com.example.bdsbe.dtos.request.PropertiesRequest;
import com.example.bdsbe.entities.posts.ResidentialProperty;
import com.example.bdsbe.repositories.posts.ResidentialPropertyRepository;
import com.longnh.exceptions.ExceptionHandle;
import com.longnh.utils.FnCommon;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ResidentialService {

  @Autowired private ResidentialPropertyRepository residentialPropertyRepository;

  @Transactional
  public ResidentialProperty create(PropertiesRequest request) {
    ResidentialProperty residentialProperty = new ResidentialProperty();
    FnCommon.copyNonNullProperties(residentialProperty, request);
    return residentialPropertyRepository.save(residentialProperty);
  }

  public ResidentialProperty getById(Long id) {
    return residentialPropertyRepository
        .findById(id)
        .orElseThrow(
            () -> new ExceptionHandle(HttpStatus.NOT_FOUND, "Không tìm thấy loại bất động sản"));
  }

  @Transactional
  public ResidentialProperty update(Long id, PropertiesRequest request) {
    ResidentialProperty residentialProperty = getById(id);
    FnCommon.copyNonNullProperties(residentialProperty, request);
    return residentialPropertyRepository.save(residentialProperty);
  }

  @Transactional
  public void delete(Long id) {
    ResidentialProperty residentialProperty = getById(id);
    residentialProperty.setIsDeleted(true);
    residentialPropertyRepository.save(residentialProperty);
  }

  public Page<ResidentialProperty> filter(PropertiesFilter request, Pageable pageable) {
    return residentialPropertyRepository.filter(request, pageable);
  }
}
