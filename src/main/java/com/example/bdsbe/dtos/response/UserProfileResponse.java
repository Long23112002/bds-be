package com.example.bdsbe.dtos.response;

import com.example.bdsbe.entities.posts.Post;
import com.example.bdsbe.utils.ResponsePage;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileResponse {

  private Integer numberOfPosts;

  private UserResponse user;

  private List<AreaOfOperation> areaOfOperations;

  private List<ResidentialOfOperation> residentialOfOperations;

  private ResponsePage<Post> posts;
}
