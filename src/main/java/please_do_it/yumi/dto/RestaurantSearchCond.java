package please_do_it.yumi.dto;

import java.util.List;
import lombok.Data;

@Data
public class RestaurantSearchCond {
  //별점 순 , 좋아요 순 이건 정렬 정보이니 뭐 상관 없지 ㅇㅇ
  private Integer startPrice; //조건 : 이거보다 크고 roe
  private Integer endPrice; //조건 : 이거보다 작음 loe
  private Boolean isOpen; // 조건 : eq
  private Boolean canPark; // 조건 : eq
  private List<String> containFoodTypes; //여러 포함음식 체크박스 중 하나 설계
  private List<String> restaurantTypes; // 조건 : forEach로 돌려가면서 ₩일치하는지
  private List<String> provideServiceTypes;
  private List<String> moodTypes;
  private Integer rate;




}
