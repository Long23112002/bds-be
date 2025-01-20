package com.example.bdsbe.dtos.response;

import com.example.bdsbe.entities.posts.Post;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelFavoriteUserResponse {

  private UserResponse user;

  private Post post;

  private Timestamp createdAt;
}
