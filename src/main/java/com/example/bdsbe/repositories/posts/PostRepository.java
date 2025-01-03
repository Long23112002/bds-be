package com.example.bdsbe.repositories.posts;

import com.example.bdsbe.entities.posts.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  @Query("SELECT p.title FROM Post p")
  List<String> suggestTitle();
}
