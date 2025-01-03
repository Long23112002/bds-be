package com.example.bdsbe.controllers;

import com.example.bdsbe.dtos.filters.PostParam;
import com.example.bdsbe.dtos.request.PostRequest;
import com.example.bdsbe.entities.posts.Post;
import com.example.bdsbe.services.posts.PostService;
import com.example.bdsbe.utils.ResponsePage;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

  @Autowired private PostService postService;

  @PostMapping
  public Post create(
      @Valid @RequestBody PostRequest request, @RequestHeader("Authorization") String token) {
    return postService.create(request, token);
  }

  @GetMapping("/{id}")
  public Post getById(@PathVariable Long id) {
    return postService.getById(id);
  }

  @GetMapping
  public ResponsePage<Post> filter(PostParam param, Pageable pageable) {
    return new ResponsePage<>(postService.filter(param, pageable));
  }

  @PutMapping("/{id}")
  public Post update(
      @PathVariable Long id,
      @Valid @RequestBody PostRequest request,
      @RequestHeader("Authorization") String token) {
    return postService.update(id, request, token);
  }

  @GetMapping("/suggest")
  public List<String> suggest() {
    return postService.suggestTitle();
  }
}
