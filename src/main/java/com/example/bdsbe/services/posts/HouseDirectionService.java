package com.example.bdsbe.services.posts;

import com.example.bdsbe.dtos.filters.PropertiesFilter;
import com.example.bdsbe.dtos.request.PropertiesRequest;
import com.example.bdsbe.entities.posts.HouseDirection;
import com.example.bdsbe.repositories.posts.HouseDirectionRepository;
import com.longnh.exceptions.ExceptionHandle;
import com.longnh.utils.FnCommon;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class HouseDirectionService {

  @Autowired private HouseDirectionRepository houseDirectionRepository;

  @Transactional
  public HouseDirection create(PropertiesRequest request) {
    HouseDirection houseDirection = new HouseDirection();
    FnCommon.copyNonNullProperties(houseDirection, request);
    return houseDirectionRepository.save(houseDirection);
  }

  public HouseDirection getById(Long id) {
    return houseDirectionRepository
        .findById(id)
        .orElseThrow(() -> new ExceptionHandle(HttpStatus.NOT_FOUND, "Không tìm thấy hướng nhà"));
  }

  @Transactional
  public HouseDirection update(Long id, PropertiesRequest request) {
    HouseDirection houseDirection = getById(id);
    FnCommon.copyNonNullProperties(houseDirection, request);
    return houseDirectionRepository.save(houseDirection);
  }

  @Transactional
  public void delete(Long id) {
    HouseDirection houseDirection = getById(id);
    houseDirection.setIsDeleted(true);
    houseDirectionRepository.save(houseDirection);
  }

  public Page<HouseDirection> filter(PropertiesFilter request, Pageable pageable) {
    return houseDirectionRepository.filter(request, pageable);
  }
}
