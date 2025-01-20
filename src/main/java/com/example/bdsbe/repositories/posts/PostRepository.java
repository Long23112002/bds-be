package com.example.bdsbe.repositories.posts;

import com.example.bdsbe.dtos.response.AreaOfOperation;
import com.example.bdsbe.dtos.response.ProvinceCountResponse;
import com.example.bdsbe.dtos.response.ResidentialOfOperation;
import com.example.bdsbe.entities.posts.Post;
import com.example.bdsbe.enums.Demand;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  @Query("SELECT p.title FROM Post p")
  List<String> suggestTitle();

  List<Post> findAllByPackagePriceTransactionIsNotNull(Pageable pageable);

  Page<Post> findByUserIdAndDemand(Long userId, Demand demand, Pageable pageable);

  @Query("SELECT COUNT(p) FROM Post p WHERE p.user.id = :userId and p.status ='APPROVED'")
  Long countByPostByUserId(Long userId);

  @Query(
      "SELECT new com.example.bdsbe.dtos.response.AreaOfOperation(pr.name, d.name, COUNT(p)) "
          + "FROM Post p "
          + "JOIN District d ON p.district.code = d.code "
          + "JOIN Province pr ON p.province.code = pr.code "
          + "WHERE p.isDeleted = false AND p.user.id = :userId "
          + "GROUP BY d.name, pr.name "
          + "ORDER BY COUNT(p) DESC")
  List<AreaOfOperation> findAreaOfOperationsByUserId(Long userId);

  @Query(
      "SELECT new com.example.bdsbe.dtos.response.ResidentialOfOperation(rp.name, COUNT(p)) "
          + "FROM Post p "
          + "JOIN ResidentialProperty rp ON p.residentialProperty.id = rp.id "
          + "WHERE p.isDeleted = false AND p.user.id = :userId "
          + "GROUP BY rp.name "
          + "ORDER BY COUNT(p) DESC")
  List<ResidentialOfOperation> findResidentialOfOperationsByUserId(Long userId);

  @Query(
      "SELECT new com.example.bdsbe.dtos.response.ProvinceCountResponse(p.province.code , p.province.name , COUNT(p)) "
          + "FROM Post p "
          + "WHERE p.isDeleted = false AND p.demand = :demand "
          + "GROUP BY p.province.code "
          + "ORDER BY COUNT(p) DESC")
  List<ProvinceCountResponse> countByProvinceCode(Demand demand);
}
