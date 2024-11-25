package please_do_it.yumi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Getter
public class RestaurantDto {
  private Long id;
  private String name;
  private Double avgRating;
  private Long reviewCount;
  private Long likeCount;
  public RestaurantDto(long id, String name, double avgRating, long reviewCount, long likeCount) {
    this.id = id;
    this.name = name;
    this.avgRating = avgRating;
    this.reviewCount = reviewCount;
    this.likeCount = likeCount;
  }
}
