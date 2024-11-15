package please_do_it.yumi.dto;

import lombok.Data;

@Data
public class RestaurantSearchCond {
  private String containFoodType; //포함 음식
  private Integer startPrice;
  private Integer endPrice;
  private Boolean isOpen;
  private Boolean canPark;
  private String restaurantType;
  private String provideServiceType;
  private String moodType;
  private Integer rate;




}
