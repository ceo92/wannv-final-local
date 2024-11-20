package please_do_it.yumi.dto;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class RestaurantSaveDto {

  private String restaurantName;
  private String businessNum;
  private List<String> restaurantTypes = new ArrayList<>();
  private List<String> containFoodTypes = new ArrayList<>();
  private List<String> provideServiceTypes = new ArrayList<>();
  private List<String> moodTypes = new ArrayList<>();
  private String roadNameAddress;
  private String landLotAddress;
  private String zipcode;
  private String detailsAddress;
  private Boolean canPark;
  private String reservationTimeGap;
  private Boolean isPenalty;
  private String image; //식당 사진

  /**
   * BusinessDay DTO
   */
  private List<LocalTime> openTimes = new ArrayList<>();
  private List<LocalTime> closeTimes = new ArrayList<>();
  private List<LocalTime> breakStartTimes = new ArrayList<>();
  private List<LocalTime> breakEndTimes = new ArrayList<>();
  private List<LocalTime> lastOrderTimes = new ArrayList<>();
  private List<Boolean> isDayOffList = new ArrayList<>();

  /**
   * Food DTO
   */
  private List<FoodSaveDto> foodSaveDtoList = new ArrayList<>();


}
