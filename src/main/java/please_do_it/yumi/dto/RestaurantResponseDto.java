package please_do_it.yumi.dto;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import please_do_it.yumi.domain.BusinessDay;
import please_do_it.yumi.domain.Review;
import please_do_it.yumi.domain.ReviewTag;

@Data
@NoArgsConstructor
public class RestaurantResponseDto {

  /**
   * Restaurant
   */
  private String restaurantName;
  private String businessNum;
  private String contact; //연락처
  //주소
  private String roadAddress;
  private String landLotAddress;
  private String detailAddress;
  private String zipCode;
  private String restaurantImage;
  private String description; //설명

  private LocalDate createdAt; //생성일
  private LocalDate updatedAt; //수정일
  private String reservationTimeGap;
  private Boolean isPenalty;
  private String businessStatus; //영업 상태 : 영업 중 , 영업 종료 , 브레이크타임
  private Boolean canPark; //주차 가능 여부

  private Set<String> containFoodTypes = new HashSet<>();
  private Set<String> provideServiceTypes = new HashSet<>();// enum
  private Set<String> restaurantTypes = new HashSet<>();
  private Set<String> moodTypes = new HashSet<>();

  /**
   * Review
   */
  private Integer rating;

  /**
   * Review => ReviewTag => Tag
   */
  private String tagCategory;
  private String tagName;

  /**
   * BusinessDay
   */
  private String dayOfWeek; //특정 요일에 대한 영업 시작 시간 ~ 라스트 오더 시간 배치할 것임
  private LocalTime openTime; //영업 시작 시간
  private LocalTime closeTime; //종료 시간
  private LocalTime breakStartTime; //브레이크 댄스 타임 시작 시간
  private LocalTime breakEndTime; //브레이크 댄스 타임 종료 시간
  private LocalTime lastOrderTime; //라스트 오더 시간
  private Boolean isDayOff; //문 닫는 날인지 , 이때는 전부 null 값임

  /**
   * Food
   */
  private String foodName;
  private String foodImage;
  private Integer foodPrice;

  /**
   * Likes
   */
  private Long likesId;

  public RestaurantResponseDto(String restaurantName, String businessNum, String contact, String roadAddress, String landLotAddress,
      String detailAddress, String zipCode, String restaurantImage, String description, LocalDate createdAt,
      LocalDate updatedAt, String reservationTimeGap, Boolean isPenalty, String businessStatus, Boolean canPark,
      Set<String> containFoodTypes, Set<String> provideServiceTypes, Set<String> restaurantTypes, Set<String> moodTypes,
      Integer rating, String tagCategory, String tagName, String dayOfWeek, LocalTime openTime, LocalTime closeTime,
      LocalTime breakStartTime, LocalTime breakEndTime, LocalTime lastOrderTime, Boolean isDayOff,
      String foodName, String foodImage, Integer foodPrice, Long likesId) {
    this.restaurantName = restaurantName;
    this.businessNum = businessNum;
    this.contact = contact;
    this.roadAddress = roadAddress;
    this.landLotAddress = landLotAddress;
    this.detailAddress = detailAddress;
    this.zipCode = zipCode;
    this.restaurantImage = restaurantImage;
    this.description = description;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.reservationTimeGap = reservationTimeGap;
    this.isPenalty = isPenalty;
    this.businessStatus = businessStatus;
    this.canPark = canPark;
    this.containFoodTypes = containFoodTypes;
    this.provideServiceTypes = provideServiceTypes;
    this.restaurantTypes = restaurantTypes;
    this.moodTypes = moodTypes;
    this.rating = rating;
    this.tagCategory = tagCategory;
    this.tagName = tagName;
    this.dayOfWeek = dayOfWeek;
    this.openTime = openTime;
    this.closeTime = closeTime;
    this.breakStartTime = breakStartTime;
    this.breakEndTime = breakEndTime;
    this.lastOrderTime = lastOrderTime;
    this.isDayOff = isDayOff;
    this.foodName = foodName;
    this.foodImage = foodImage;
    this.foodPrice = foodPrice;
    this.likesId = likesId;
  }



}
