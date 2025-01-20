package com.example.bdsbe.entities.primaryKey;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelFavoriteUserId implements Serializable {
  private Long userId;

  private Long postId;
}
