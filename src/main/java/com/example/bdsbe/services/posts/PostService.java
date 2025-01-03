package com.example.bdsbe.services.posts;

import com.example.bdsbe.dtos.filters.PostParam;
import com.example.bdsbe.dtos.request.PostRequest;
import com.example.bdsbe.dtos.response.JwtResponse;
import com.example.bdsbe.entities.posts.PackagePriceTransaction;
import com.example.bdsbe.entities.posts.Post;
import com.example.bdsbe.enums.Demand;
import com.example.bdsbe.enums.PostStatus;
import com.example.bdsbe.enums.Unit;
import com.example.bdsbe.repositories.packages.PackagePriceTransactionRepository;
import com.example.bdsbe.repositories.posts.PostRepository;
import com.example.bdsbe.repositories.users.UserRepository;
import com.example.bdsbe.services.packages.PackageTransactionService;
import com.example.bdsbe.services.users.JwtService;
import com.longnh.exceptions.ExceptionHandle;
import com.longnh.utils.FnCommon;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

  @Autowired private PostRepository postRepository;
  @Autowired private InteriorService interiorService;
  @Autowired private HouseDirectionService houseDirectionService;
  @Autowired private BalconyDirectionService balconyDirectionService;
  @Autowired private PropertyLegalDocumentService propertyLegalDocumentService;
  @Autowired private ResidentialService residentialService;
  @Autowired private UserRepository userRepository;
  @Autowired private PackageTransactionService packageTransactionService;
  @Autowired private JwtService jwtService;
  @Autowired private EntityManager entityManager;
  @Autowired private PackagePriceTransactionRepository packagePriceTransactionRepository;

  @Transactional(rollbackFor = Exception.class)
  public Post create(PostRequest request, String token) {
    try {
      Post post = new Post();

      buildPost(request, post, jwtService.decodeToken(token));

      post.setStatus(PostStatus.PENDING);
      if (Objects.equals(request.getUnit(), Unit.VND)) {
        post.setPrice(request.getPrice() * request.getArena());
      } else {
        post.setPrice(request.getPrice());
      }
      post.setPrice(request.getPrice() * request.getArena());
      post = postRepository.save(post);
      PackagePriceTransaction packagePriceTransaction =
          packageTransactionService.save(request, token, post.getId());
      post.setCodePlant(packagePriceTransaction.getPackagePrice().getAPackage().getCode());
      post.setPackagePriceTransaction(packagePriceTransaction);
      post = postRepository.save(post);

      return post;

    } catch (ExceptionHandle e) {
      e.printStackTrace();
      throw new ExceptionHandle(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  public Post getById(Long id) {
    return postRepository
        .findById(id)
        .orElseThrow(() -> new ExceptionHandle(HttpStatus.NOT_FOUND, "Không tìm thấy bài đăng"));
  }

  public Post update(Long id, PostRequest request, String token) {
    Post post = getById(id);
    buildPost(request, post, jwtService.decodeToken(token));
    return postRepository.save(post);
  }

  public List<String> suggestTitle() {
    return postRepository.suggestTitle();
  }

  public void delete(Long id) {
    Post post = getById(id);
    post.setIsDeleted(true);
    postRepository.save(post);
  }

  public Page<Post> filter(PostParam postParam, Pageable pageable) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Post> cq = cb.createQuery(Post.class);
    Root<Post> root = cq.from(Post.class);

    List<Predicate> predicates = new ArrayList<>();

    if (postParam.getMinPrice() != null) {
      predicates.add(cb.greaterThanOrEqualTo(root.get("price"), postParam.getMinPrice()));
    }
    if (postParam.getMaxPrice() != null) {
      predicates.add(cb.lessThanOrEqualTo(root.get("price"), postParam.getMaxPrice()));
    }
    if (postParam.getProvinceCode() != null) {
      predicates.add(cb.equal(root.get("provinceCode"), postParam.getProvinceCode()));
    }
    if (postParam.getDistrictCode() != null) {
      predicates.add(cb.equal(root.get("districtCode"), postParam.getDistrictCode()));
    }
    if (postParam.getWardCode() != null) {
      predicates.add(cb.equal(root.get("wardCode"), postParam.getWardCode()));
    }
    if (postParam.getKeyword() != null) {
      String keyword = postParam.getKeyword().toLowerCase();
      predicates.add(cb.or(cb.like(cb.lower(root.get("title")), "%" + keyword + "%")));
    }

    if (postParam.getMinArea() != null) {
      predicates.add(cb.greaterThanOrEqualTo(root.get("arena"), postParam.getMinArea()));
    }
    if (postParam.getMaxArea() != null) {
      predicates.add(cb.lessThanOrEqualTo(root.get("arena"), postParam.getMaxArea()));
    }

    if (postParam.getStatus() != null) {
      predicates.add(cb.equal(root.get("status"), postParam.getStatus()));
    }

    if (postParam.getDemand() != null) {
      predicates.add(cb.equal(root.get("demand"), postParam.getDemand()));
    }

    if (postParam.getResidentialPropertyIds() != null
        && !postParam.getResidentialPropertyIds().isEmpty()) {
      predicates.add(
          root.get("residentialProperty").get("id").in(postParam.getResidentialPropertyIds()));
    }
    if (postParam.getHouseDirectionIds() != null && !postParam.getHouseDirectionIds().isEmpty()) {
      predicates.add(root.get("houseDirection").get("id").in(postParam.getHouseDirectionIds()));
    }
    if (postParam.getBalconyDirectionIds() != null
        && !postParam.getBalconyDirectionIds().isEmpty()) {
      predicates.add(root.get("balconyDirection").get("id").in(postParam.getBalconyDirectionIds()));
    }
    if (postParam.getNumBerOfBedrooms() != null && !postParam.getNumBerOfBedrooms().isEmpty()) {
      predicates.add(root.get("numberOfBedrooms").in(postParam.getNumBerOfBedrooms()));
    }

    if (postParam.getCodePlant() != null) {
      predicates.add(cb.equal(root.get("codePlant"), postParam.getCodePlant()));
    }

    cq.where(predicates.toArray(new Predicate[0]));

    cq.orderBy(cb.desc(root.get("createdAt")));

    TypedQuery<Post> query = entityManager.createQuery(cq);

    CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
    Root<Post> countRoot = countQuery.from(Post.class);
    countQuery.select(cb.count(countRoot)).where(predicates.toArray(new Predicate[0]));
    Long total = entityManager.createQuery(countQuery).getSingleResult();
    query.setFirstResult((int) pageable.getOffset());
    query.setMaxResults(pageable.getPageSize());

    List<Post> posts = query.getResultList();

    return new PageImpl<>(posts, pageable, total);
  }

  private void buildPost(PostRequest request, Post post, JwtResponse jwtResponse) {
    FnCommon.copyNonNullProperties(post, request);
    post.setInterior(interiorService.getById(request.getInteriorId()));
    post.setHouseDirection(houseDirectionService.getById(request.getHouseDirectionId()));
    post.setBalconyDirection(balconyDirectionService.getById(request.getBalconyDirectionId()));
    post.setPropertyLegalDocument(
        propertyLegalDocumentService.getById(request.getPropertyLegalDocumentId()));
    post.setUser(userRepository.getById(jwtResponse.getUserId()));
    post.setResidentialProperty(residentialService.getById(request.getResidentialPropertyId()));
    if (request.getDemand().equals(Demand.SELL)) {
      post.setFront(0.0);
      post.setEntrance(0.0);
    } else {
      post.setEntrance(request.getEntrance());
      post.setFront(request.getFront());
    }
  }
}
