package please_do_it.yumi.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import please_do_it.yumi.constant.BusinessStatus;

@Entity
@Getter @Setter
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
public class Restaurant {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String contact;

  @Embedded
  private Address address;
  private String image;
  private String description;

  private Double rateAverage; //평균 별점 높은 순
  private Integer likesCount; //좋아요 많은 순
  private Integer reviewCount; //리뷰 많은 순

  private LocalDate createdAt;
  private LocalDate updatedAt;


  @Enumerated(EnumType.STRING)
  private BusinessStatus businessStatus; //영업 상태 : 영업 중 , 영업 종료 , 브레이크타임

  private boolean canPark; //주차 가능 여부


  @OneToMany(mappedBy = "restaurant")
  private List<Review> reviews = new ArrayList<>(); //해당 식당에서 작성한 사용자들의 리뷰를 담을  것임


  //오직 Restaurant 부모에게만 Food는 의존되므로 cascade , orphanRemoval 걸었음 , cascade , orphanRemoval 특징 : 리포지토리 없어도 됨 즉 em.perist(BusinessDay)하지 않아도 연쇄적으로 알아서 저장됨 , 생명주기가 전부 rESTAURANT에 의존되었기 때문!
  @OneToMany(mappedBy = "restaurant" , cascade = CascadeType.ALL , orphanRemoval = true)
  private List<BusinessDay> businessDays = new ArrayList<>();


  //오직 Restaurant 부모에게만 Food는 의존되므로 cascade , orphanRemoval 걸었음 ,
  @OneToMany(mappedBy = "restaurant" , cascade = CascadeType.ALL , orphanRemoval = true)
  private List<Food> foods = new ArrayList<>();

  @OneToMany(mappedBy = "restaurant")
  private List<Likes> likes = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;



  /**
   * 체크박스 , 동적 검색조건 데이터 , 변경할 일 없으므로 @ElementCollection 정의
   */
  //여러 포함 음식 종류들(유제품 , 계란 , ...) ContaintFoodType ,조회 : 지연로딩 , 필요한 시점에 조회되게 저장 시 cascade로 연달아 저장됨 ㅇㅇ , 즉 알바없음 !
  // 기본적으로 cascade , orphanRemoval 걸려있음

  @ElementCollection
  @CollectionTable(name = "ContainFoodType", joinColumns = @JoinColumn(name = "id"))
  private List<String> containFoodTypes = new ArrayList<>();


  //여러 제공하는 서비스 종류들(단체석 이용 가능 , 무선 와이파이 존재 , 콜키지 가능 , ...) ProvideServiceType
  @ElementCollection
  @CollectionTable(name = "ProvideServiceType", joinColumns = @JoinColumn(name = "id"))
  private List<String> provideServiceTypes = new ArrayList<>();// enum


  //주로 파는 품목 카테고리(추후 단일 객체 고려)RestaurantType
  @ElementCollection
  @CollectionTable(name = "RestaurantType", joinColumns = @JoinColumn(name = "id"))
  private List<String> restaurantTypes = new ArrayList<>();


  @ElementCollection
  @CollectionTable(name = "MoodType", joinColumns = @JoinColumn(name = "id"))
  private List<String> moodTypes = new ArrayList<>();

  /**
   * 복잡한 연관관계 => 생성 메서드 , 다른 개발자들이 해당 틀대로 생성하게끔 유도하기 !
   */
  public Restaurant createRestaurant(String businessUserName , String businessNum, String restaurantName , List<String> moodTypes ,
      List<String> containFoodTypes , List<String> provideServiceTypes , List<String> restaurantTypes , String image , String roadNameAddress
      , String zipcode , String detailsAddress , Boolean canPark , String reservationTimeGap , String isPenalty , List<BusinessDay> businessDays){

    Restaurant restaurant = new Restaurant();
    restaurant.setUser();

  }




  /**
   * 도메인 모델 패턴 : 비즈니스 로직 정의(서비스가 아닌 도메인에 정의하기)
   * DDD로 하면 단위 테스트에서 객체 생성만으로 테스트도 가능한 유라함도 가져갈 수 있음
   */

  public double averageRate(){
    return reviews.stream().mapToInt(Review::getRate).average().getAsDouble(); //평균 계산
  }

  public int totalReviewCount(){
    return reviews.size();
  }


  //상태 설정 메서드로 가자
  public void changeBusinessStatus(BusinessStatus businessStatus){
    this.businessStatus = businessStatus;
  }


  //이건 서비스 단에서 처리해주는 게 맞는 거 같은데 ..
  public void calculateBusinessStatus(){
    LocalDateTime now = LocalDateTime.now();
    String nowDayOfWeek = now.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN); //요일 정보(월요일 , ... , 일요일 ) 담김
    BusinessDay businessDay = businessDays.stream()
        .filter(bd -> bd.getDayOfWeek().equals(nowDayOfWeek)).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 요일입니다."));
    if (businessDay.getIsDayOff()){
      this.businessStatus = BusinessStatus.TODAY_BREAK;
    }

    LocalTime openTime = businessDay.getOpenTime();
    LocalTime closeTime = businessDay.getCloseTime();
    if (now.toLocalTime().isAfter(openTime) && now.toLocalTime().isBefore(closeTime)) {
      LocalTime breakStartTime = businessDay.getBreakStartTime();
      LocalTime breakEndTime = businessDay.getBreakEndTime();
      if (now.toLocalTime().isAfter(breakStartTime) && now.toLocalTime().isBefore(breakEndTime)) {
        this.businessStatus = BusinessStatus.BREAK_TIME;
      }
      this.businessStatus = BusinessStatus.OPEN;
    }
    else {
      this.businessStatus = BusinessStatus.CLOSE;
    }





  }




}
