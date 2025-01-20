package com.example.bdsbe.controllers;

import com.example.bdsbe.dtos.request.FavoriteRequest;
import com.example.bdsbe.dtos.response.RelFavoriteUserResponse;
import com.example.bdsbe.services.favorites.RelFavoriteUserService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/favorites")
public class FavoriteController {

  @Autowired private RelFavoriteUserService relFavoriteUserService;

  @GetMapping
  public List<RelFavoriteUserResponse> filter(@RequestHeader("Authorization") String token) {
    return relFavoriteUserService.filterFavorite(token);
  }

  @PostMapping
  public RelFavoriteUserResponse save(
      @Valid @RequestBody FavoriteRequest request, @RequestHeader("Authorization") String token) {
    return relFavoriteUserService.save(request, token);
  }

  @DeleteMapping("/{postId}")
  public void delete(@PathVariable Long postId, @RequestHeader("Authorization") String token) {
    relFavoriteUserService.delete(postId, token);
  }
}
