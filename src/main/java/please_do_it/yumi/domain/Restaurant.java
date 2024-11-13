package please_do_it.yumi.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.Getter;
import lombok.Setter;
import please_do_it.yumi.constant.BusinessStatus;

@Entity
@Getter @Setter
public class Restaurant {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String contact;

  @Embedded
  private Address address;
  private String image;
  private String description;


  @Enumerated(EnumType.STRING)
  private BusinessStatus businessStatus; //영업 상태 : 영업 중 , 영업 종료 , 브레이크타임

  private boolean canPark; //주차 가능 여부


  @OneToMany(mappedBy = "restaurant")
  private List<Review> reviews = new ArrayList<>(); //해당 식당에서 작성한 사용자들의 리뷰를 담을  것임


  @OneToMany(mappedBy = "restaurant")
  private List<BusinessDay> businessDays = new ArrayList<>();


  @OneToMany(mappedBy = "restaurant")
  private List<Food> foods = new ArrayList<>();


  //여러 리뷰 태그들 Review 태그들 , reviews를 통해 나온 리뷰 태그들 top5만 가져와서 해당 기반으로 출력하게 할 것임 ㅇㅇ
  //private List<String> reviewTag = new ArrayList<>(); //임의로 여러 리뷰 타입을 갖게 할 것임 , 사장님이 친절해요 , 맛있어요 , 인테리어 유미 쪽에서 등록 ㅇㅇ


  /**
   * 체크박스 , 동적 검색조건 데이터 , 변경할 일 없으므로 @ElementCollection 정의
   */
  //여러 포함 음식 종류들(유제품 , 계란 , ...) ContaintFoodType
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
  private List<String> moodType = new ArrayList<>();


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
    if (businessDay.getIsClose()){
      this.businessStatus = BusinessStatus.TODAY_BREAK;
    }

    LocalDateTime openTime = businessDay.getOpenTime();
    LocalDateTime closeTime = businessDay.getCloseTime();
    if (now.isAfter(openTime) && now.isBefore(closeTime)) {
      LocalDateTime breakStartTime = businessDay.getBreakStartTime();
      LocalDateTime breakEndTime = businessDay.getBreakEndTime();
      if (now.isAfter(breakStartTime) && now.isBefore(breakEndTime)) {
        this.businessStatus = BusinessStatus.BREAK_TIME;
      }
      this.businessStatus = BusinessStatus.OPEN;
    }
    else {
      this.businessStatus = BusinessStatus.CLOSE;
    }





  }




}
