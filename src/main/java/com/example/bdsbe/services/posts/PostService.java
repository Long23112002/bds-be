package com.example.bdsbe.services.posts;

import com.example.bdsbe.dtos.filters.PostParam;
import com.example.bdsbe.dtos.request.PostRequest;
import com.example.bdsbe.dtos.response.JwtResponse;
import com.example.bdsbe.dtos.response.MessageResponse;
import com.example.bdsbe.dtos.response.ProvinceCountResponse;
import com.example.bdsbe.entities.posts.LogsTransaction;
import com.example.bdsbe.entities.posts.PackagePriceTransaction;
import com.example.bdsbe.entities.posts.Post;
import com.example.bdsbe.entities.publics.District;
import com.example.bdsbe.entities.publics.Province;
import com.example.bdsbe.entities.publics.Ward;
import com.example.bdsbe.enums.Demand;
import com.example.bdsbe.enums.PostStatus;
import com.example.bdsbe.enums.Unit;
import com.example.bdsbe.repositories.packages.PackagePriceTransactionRepository;
import com.example.bdsbe.repositories.posts.LogTransactionRepository;
import com.example.bdsbe.repositories.posts.PostRepository;
import com.example.bdsbe.repositories.publics.DistrictRepository;
import com.example.bdsbe.repositories.publics.ProvinceRepository;
import com.example.bdsbe.repositories.publics.WardRepository;
import com.example.bdsbe.repositories.users.UserRepository;
import com.example.bdsbe.services.bots.TelegramBotService;
import com.example.bdsbe.services.packages.PackageTransactionService;
import com.example.bdsbe.services.users.JwtService;
import com.longnh.exceptions.ExceptionHandle;
import com.longnh.utils.FnCommon;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
  @Autowired private TelegramBotService telegramBotService;
  @Autowired private WardRepository wardRepository;
  @Autowired private ProvinceRepository provinceRepository;
  @Autowired private DistrictRepository districtRepository;
  @Autowired private PackagePriceTransactionRepository packagePriceTransactionRepository;
  @Autowired private LogTransactionRepository logTransactionRepository;

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

      telegramBotService.sendMessageWithButtons(
          post.getId().toString(),
          post.getTitle(),
          post.getContactName(),
          post.getPhoneNumber(),
          post.getPackagePriceTransaction().getPackagePrice().getAPackage().getName(),
          post.getPackagePriceTransaction().getStartDate().toString(),
          post.getPackagePriceTransaction().getEndDate().toString(),
          "lINK:" + post.getId().toString());
      return post;

    } catch (ExceptionHandle e) {
      e.printStackTrace();
      throw new ExceptionHandle(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  @Transactional
  public MessageResponse approvedPost(Long id, PostStatus postStatus, Long chatId, String token) {
    Post post = getById(id);

    if (logTransactionRepository.existsByPackagePriceTransactionId(
        post.getPackagePriceTransaction().getId())) {
      return new MessageResponse(
          "Mã tin :"
              + post.getId()
              + " đã được xử lý trước đó \nnếu muốn sửa lại trạng thái vui lòng truy cập webside của admin \n"
              + "Link: http://localhost:8080/admin");
    }

    PostStatus previousStatus = post.getStatus();
    PackagePriceTransaction postTransaction = post.getPackagePriceTransaction();

    postTransaction.setStatus(postStatus);
    post.setStatus(postStatus);
    packagePriceTransactionRepository.save(postTransaction);
    postRepository.save(post);

    if (token != null && !token.trim().isEmpty()) {
      JwtResponse jwtResponse = jwtService.decodeToken(token);
      saveLogPostApprove(
          postStatus,
          previousStatus,
          post.getPackagePriceTransaction().getPackagePrice().getPrice(),
          jwtResponse.getUserId(),
          postTransaction);
    } else {
      saveLogPostApprove(
          postStatus,
          previousStatus,
          post.getPackagePriceTransaction().getPackagePrice().getPrice(),
          chatId,
          postTransaction);
    }
    if (postStatus.equals(PostStatus.APPROVED)) {
      return new MessageResponse("Mã tin :" + post.getId() + " đã được duyệt");
    }
    return new MessageResponse("Mã tin :" + post.getId() + " đã được hủy duyệt");
  }

  @Transactional
  public void saveLogPostApprove(
      PostStatus newStatus,
      PostStatus oldStatus,
      Double price,
      Long userApproved,
      PackagePriceTransaction packagePriceTransaction) {
    LogsTransaction logsTransaction = new LogsTransaction();
    logsTransaction.setPrice(price);
    logsTransaction.setOldStatus(oldStatus);
    logsTransaction.setStatus(newStatus);
    logsTransaction.setUserApproved(userApproved);
    logsTransaction.setPackagePriceTransaction(packagePriceTransaction);
    logTransactionRepository.save(logsTransaction);
  }

  public Long countPostById(String token) {
    JwtResponse jwtResponse = jwtService.decodeToken(token);
    return postRepository.countByPostByUserId(jwtResponse.getUserId());
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
      predicates.add(cb.equal(root.get("province").get("code"), postParam.getProvinceCode()));
    }
    if (postParam.getDistrictCode() != null) {
      predicates.add(cb.equal(root.get("district").get("code"), postParam.getDistrictCode()));
    }
    if (postParam.getWardCode() != null) {
      predicates.add(cb.equal(root.get("ward").get("code"), postParam.getWardCode()));
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

    if (postParam.getIsExpired() != null) {
      LocalDate now = LocalDate.now();
      if (postParam.getIsExpired()) {
        // Nếu getIsExpired() = true, lấy cả bài viết đã hết hạn và chưa hết hạn
        // Không cần thêm điều kiện về ngày hết hạn
      } else {
        // Nếu getIsExpired() = false, chỉ lấy bài viết chưa hết hạn
        predicates.add(
            cb.greaterThanOrEqualTo(root.get("packagePriceTransaction").get("endDate"), now));
      }
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

    if (postParam.getUserId() != null) {
      predicates.add(root.get("user").get("id").in(postParam.getUserId()));
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
    post.setProvince(getProvinceByCode(request.getProvinceCode()));
    post.setDistrict(getDistrictByCode(request.getDistrictCode()));
    post.setWard(getWardByCode(request.getWardCode()));
    post.setResidentialProperty(residentialService.getById(request.getResidentialPropertyId()));
    if (request.getDemand().equals(Demand.SELL)) {
      post.setFront(0.0);
      post.setEntrance(0.0);
    } else {
      post.setEntrance(request.getEntrance());
      post.setFront(request.getFront());
    }
  }

  private Ward getWardByCode(String wardCode) {
    return wardRepository.findByCode(wardCode);
  }

  private District getDistrictByCode(String districtCode) {
    return districtRepository.findByCode(districtCode);
  }

  private Province getProvinceByCode(String provinceCode) {
    return provinceRepository.findByCode(provinceCode);
  }

  public List<ProvinceCountResponse> countProvince(Demand demand) {
    return postRepository.countByProvinceCode(demand);
  }
}
