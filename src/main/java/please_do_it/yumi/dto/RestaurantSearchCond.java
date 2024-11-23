package please_do_it.yumi.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantSearchCond {

  /**
   * where 동적 조건
   */
  //별점 순 , 좋아요 순 이건 정렬 정보이니 뭐 상관 없지 ㅇㅇ
  private Integer startPrice; //조건 : 이거보다 크고 roe
  private Integer endPrice; //조건 : 이거보다 작음 loe
  private Boolean isOpen = false; // 조건 : eq
  private Boolean canPark = false; // 조건 : eq
  private String roadAddress;

  private List<Integer> rates = new ArrayList<>(); //별점
  private Set<String> containFoodTypes = new HashSet<>(); //여러 포함음식 체크박스 중 하나 설계
  private Set<String> restaurantTypes = new HashSet<>(); // 조건 : forEach로 돌려가면서 ₩일치하는지
  private Set<String> provideServiceTypes = new HashSet<>();
  private Set<String> moodTypes = new HashSet<>();

  /**
   * orderBy 동적 조건
   */
  private Boolean isCreatedAtChecked = false; //등록일자 : 최신 순 정렬 위함
  private Boolean isRateChecked = false; // 평균 별점 높은 순 정렬 위함
  private Boolean isLikesChecked = false; // 좋아요 많은 순 정렬 위함
  private Boolean isReviewCountChecked = false; // 리뷰 많은 순 정렬 위함




}
