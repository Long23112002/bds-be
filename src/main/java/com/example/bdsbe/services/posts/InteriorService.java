package com.example.bdsbe.services.posts;

import com.example.bdsbe.dtos.filters.PropertiesFilter;
import com.example.bdsbe.dtos.request.PropertiesRequest;
import com.example.bdsbe.entities.posts.Interior;
import com.example.bdsbe.repositories.posts.InteriorRepository;
import com.longnh.exceptions.ExceptionHandle;
import com.longnh.utils.FnCommon;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class InteriorService {

  @Autowired private InteriorRepository interiorRepository;

  @Transactional
  public Interior create(PropertiesRequest request) {
    Interior interior = new Interior();
    FnCommon.copyNonNullProperties(interior, request);
    return interiorRepository.save(interior);
  }

  public Interior getById(Long id) {
    return interiorRepository
        .findById(id)
        .orElseThrow(() -> new ExceptionHandle(HttpStatus.NOT_FOUND, "Không tìm thấy nội thất"));
  }

  @Transactional
  public Interior update(Long id, PropertiesRequest request) {
    Interior interior = getById(id);
    FnCommon.copyNonNullProperties(interior, request);
    return interiorRepository.save(interior);
  }

  @Transactional
  public void delete(Long id) {
    Interior interior = getById(id);
    interior.setIsDeleted(true);
    interiorRepository.save(interior);
  }

  public Page<Interior> filter(PropertiesFilter request, Pageable pageable) {
    return interiorRepository.filter(request, pageable);
  }
}
