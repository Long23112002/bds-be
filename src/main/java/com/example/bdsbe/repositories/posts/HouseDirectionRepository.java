package com.example.bdsbe.repositories.posts;

import com.example.bdsbe.dtos.filters.PropertiesFilter;
import com.example.bdsbe.entities.posts.HouseDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseDirectionRepository extends JpaRepository<HouseDirection, Long> {

  @Query(
      "SELECT p FROM HouseDirection p "
          + "WHERE cast(:#{#filter.name} as string ) IS NULL OR "
          + "LOWER(FUNCTION('unaccent', p.name)) LIKE CONCAT('%', LOWER(FUNCTION('unaccent', cast(:#{#filter.name} as string ))), '%')")
  Page<HouseDirection> filter(PropertiesFilter filter, Pageable pageable);
}
