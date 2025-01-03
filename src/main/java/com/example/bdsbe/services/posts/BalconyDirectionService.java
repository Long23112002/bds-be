package com.example.bdsbe.services.posts;

import com.example.bdsbe.dtos.filters.PropertiesFilter;
import com.example.bdsbe.dtos.request.PropertiesRequest;
import com.example.bdsbe.entities.posts.BalconyDirection;
import com.example.bdsbe.repositories.posts.BalconyDirectionRepository;
import com.longnh.exceptions.ExceptionHandle;
import com.longnh.utils.FnCommon;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class BalconyDirectionService {

  @Autowired private BalconyDirectionRepository balconyDirectionRepository;

  @Transactional
  public BalconyDirection create(PropertiesRequest request) {
    BalconyDirection balconyDirection = new BalconyDirection();
    FnCommon.copyNonNullProperties(balconyDirection, request);
    return balconyDirectionRepository.save(balconyDirection);
  }

  public BalconyDirection getById(Long id) {
    return balconyDirectionRepository
        .findById(id)
        .orElseThrow(
            () -> new ExceptionHandle(HttpStatus.NOT_FOUND, "Không tìm thấy hướng ban công"));
  }

  @Transactional
  public BalconyDirection update(Long id, PropertiesRequest request) {
    BalconyDirection balconyDirection = getById(id);
    FnCommon.copyNonNullProperties(balconyDirection, request);
    return balconyDirectionRepository.save(balconyDirection);
  }

  @Transactional
  public void delete(Long id) {
    BalconyDirection balconyDirection = getById(id);
    balconyDirection.setIsDeleted(true);
    balconyDirectionRepository.save(balconyDirection);
  }

  public Page<BalconyDirection> filter(PropertiesFilter request, Pageable pageable) {
    return balconyDirectionRepository.filter(request, pageable);
  }
}
