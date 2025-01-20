package com.example.bdsbe.services.favorites;

import com.example.bdsbe.dtos.request.FavoriteRequest;
import com.example.bdsbe.dtos.response.JwtResponse;
import com.example.bdsbe.dtos.response.RelFavoriteUserResponse;
import com.example.bdsbe.dtos.response.UserResponse;
import com.example.bdsbe.entities.favorites.RelFavoriteUser;
import com.example.bdsbe.entities.posts.Post;
import com.example.bdsbe.entities.primaryKey.RelFavoriteUserId;
import com.example.bdsbe.entities.users.User;
import com.example.bdsbe.repositories.favorites.RelFavoriteUserRepository;
import com.example.bdsbe.repositories.users.UserRepository;
import com.example.bdsbe.services.posts.PostService;
import com.example.bdsbe.services.users.AuthenticationService;
import com.example.bdsbe.services.users.JwtService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelFavoriteUserService {

  @Autowired private RelFavoriteUserRepository relFavoriteUserRepository;
  @Autowired private AuthenticationService authenticationService;
  @Autowired private PostService postService;
  @Autowired private JwtService jwtService;
  @Autowired private UserRepository userRepository;

  public RelFavoriteUserResponse save(FavoriteRequest request, String token) {
    JwtResponse jwtResponse = jwtService.decodeToken(token);
    Post post = postService.getById(request.getPostId());
    User user = userRepository.getById(jwtResponse.getUserId());
    RelFavoriteUserId id = new RelFavoriteUserId(jwtResponse.getUserId(), post.getId());

    boolean exists = relFavoriteUserRepository.existsById(id);
    if (exists) {
      //            throw new IllegalArgumentException("This post is already marked as favorite by
      // the user.");
    }

    RelFavoriteUser relFavoriteUser =
        relFavoriteUserRepository.save(RelFavoriteUser.builder().id(id).build());

    return new RelFavoriteUserResponse(
        mapUserToUserResponse(user), post, relFavoriteUser.getCreatedAt());
  }

  public void delete(Long postId, String token) {

    JwtResponse jwtResponse = jwtService.decodeToken(token);

    Long userId = jwtResponse.getUserId();

    Post post = postService.getById(postId);

    RelFavoriteUserId id = new RelFavoriteUserId(userId, post.getId());

    boolean exists = relFavoriteUserRepository.existsById(id);
    if (!exists) {
      throw new IllegalArgumentException("Favorite entry not found for the given user and post.");
    }

    relFavoriteUserRepository.deleteById(id);
  }

  public List<RelFavoriteUserResponse> filterFavorite(String token) {
    JwtResponse jwtResponse = jwtService.decodeToken(token);
    Long userId = jwtResponse.getUserId();
    List<RelFavoriteUser> relFavoriteUsers = relFavoriteUserRepository.findAllByIdUserId(userId);
    return convertToResponse(relFavoriteUsers);
  }

  private List<RelFavoriteUserResponse> convertToResponse(List<RelFavoriteUser> relFavoriteUsers) {
    return relFavoriteUsers.stream()
        .map(
            relFavoriteUser -> {
              User user = userRepository.getById(relFavoriteUser.getId().getUserId());
              Post post = postService.getById(relFavoriteUser.getId().getPostId());
              return new RelFavoriteUserResponse(
                  mapUserToUserResponse(user), post, relFavoriteUser.getCreatedAt());
            })
        .collect(Collectors.toList());
  }

  private UserResponse mapUserToUserResponse(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .email(user.getEmail())
        .fullName(user.getFullName())
        .phoneNumber(user.getPhoneNumber())
        .avatar(user.getAvatar())
        .wallet(user.getWallet())
        .isAdmin(user.getIsAdmin())
        .build();
  }
}
