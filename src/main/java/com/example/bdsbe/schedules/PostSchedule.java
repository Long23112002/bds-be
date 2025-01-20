package com.example.bdsbe.schedules;

import com.example.bdsbe.entities.posts.Post;
import com.example.bdsbe.enums.PostStatus;
import com.example.bdsbe.repositories.posts.PostRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostSchedule {

  @Autowired private PostRepository postRepository;

  @Async
  @Scheduled(cron = "0 * * * * *")
  @Transactional
  public void checkExpiredPackageTransactions() {
    int pageNumber = 0;
    Pageable pageable = PageRequest.of(pageNumber, 100);

    while (true) {
      List<Post> posts = postRepository.findAllByPackagePriceTransactionIsNotNull(pageable);
      if (posts.isEmpty()) {
        break;
      }

      LocalDateTime now = LocalDateTime.now();
      for (Post post : posts) {
        if (post.getPackagePriceTransaction() != null
            && post.getPackagePriceTransaction().getEndDate() != null
            && post.getPackagePriceTransaction().getStartTime() != null) {
          LocalDate endDate = post.getPackagePriceTransaction().getEndDate();
          LocalTime startTime = post.getPackagePriceTransaction().getStartTime();

          LocalDateTime endDateTime = endDate.atTime(startTime);

          if (endDateTime.isBefore(now)) {
            post.setStatus(PostStatus.EXPIRED);
            postRepository.save(post);
          }
        }
      }
      pageNumber++;
      pageable = PageRequest.of(pageNumber, 100);
    }
  }
}
