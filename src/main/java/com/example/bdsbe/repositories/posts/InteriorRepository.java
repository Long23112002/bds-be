package com.example.bdsbe.repositories.posts;

import com.example.bdsbe.dtos.filters.PropertiesFilter;
import com.example.bdsbe.entities.posts.Interior;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InteriorRepository extends JpaRepository<Interior, Long> {

  @Query(
      "SELECT p FROM Interior p "
          + "WHERE cast(:#{#filter.name} as string ) IS NULL OR "
          + "LOWER(FUNCTION('unaccent', p.name)) LIKE CONCAT('%', LOWER(FUNCTION('unaccent',  cast(:#{#filter.name} as string ))), '%')")
  Page<Interior> filter(PropertiesFilter filter, Pageable pageable);
}
