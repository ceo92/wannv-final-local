package please_do_it.yumi.dto;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantUpdateDto {
  private String restaurantName;
  private String businessNum;
  private Set<String> restaurantTypes = new HashSet<>();
  private Set<String> containFoodTypes = new HashSet<>();
  private Set<String> provideServiceTypes = new HashSet<>();
  private Set<String> moodTypes = new HashSet<>();
  private String roadNameAddress;
  private String landLotAddress;
  private String zipcode;
  private String detailAddress;
  private Boolean canPark;
  private String reservationTimeGap;
  private Boolean isPenalty;
  private List<MultipartFile> restaurantImages = new ArrayList<>(); // 식당 사진 폼에서 꺼내기
  private List<String> restaurantImagesUrl = new ArrayList<>(); //식당 스토리지에 저장 후 URL을 DB에 저장용
  /**
   * BusinessDay DTO
   */
  private List<LocalTime> openTimes = new ArrayList<>();
  private List<LocalTime> closeTimes = new ArrayList<>();
  private List<LocalTime> breakStartTimes = new ArrayList<>();
  private List<LocalTime> breakEndTimes = new ArrayList<>();
  private List<LocalTime> lastOrderTimes = new ArrayList<>();
  private List<String> isDayOffList = new ArrayList<>();

  /**
   * Food DTO
   */
  private List<FoodUpdateDto> foodSaveDtoList = new ArrayList<>();

  public RestaurantUpdateDto(String name, String businessNum, Set<String> restaurantTypes, Set<String> containFoodTypes, Set<String> provideServiceTypes, Set<String> moodTypes, String roadAddress, String landLotAddress, String zipCode, String detailAddress, Boolean canPark, int reservationTimeGap, Boolean isPenalty) {
    this.restaurantName = name;
    this.businessNum = businessNum;
    this.restaurantTypes = restaurantTypes;
    this.containFoodTypes = containFoodTypes;
    this.provideServiceTypes = provideServiceTypes;
    this.moodTypes = moodTypes;
    this.roadNameAddress = roadAddress;
    this.landLotAddress = landLotAddress;
    this.zipcode = zipCode;
    this.detailAddress = detailAddress;
    this.canPark = canPark;
    //this.reservationTimeGap = reservationTimeGap;
    this.isPenalty = isPenalty;
  }
}
