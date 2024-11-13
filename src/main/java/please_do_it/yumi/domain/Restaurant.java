package please_do_it.yumi.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter(AccessLevel.PRIVATE)
public class Restaurant {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String contact;

  @Embedded
  private Address address;
  private String image;
  private String description;

  private boolean isOpen; //영업 중인지 아닌지 체크박스
  private boolean isPark; //주차 가능한지 불가능한지 체크박스


  @OneToMany(mappedBy = "restaurant")
  private List<Review> reviews = new ArrayList<>(); //해당 식당에서 작성한 사용자들의 리뷰를 담을  것임



  @OneToMany(mappedBy = "restaurant")
  private List<BusinessDay> businessDays = new ArrayList<>();


  //여러 리뷰 태그들 Review 태그들
  private List<String> reviewTag = new ArrayList<>(); //임의로 여러 리뷰 타입을 갖게 할 것임 , 사장님이 친절해요 , 맛있어요 , 인테리어 유미 쪽에서 등록 ㅇㅇ



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



  private boolean canPark; //주차 가능 여부





  public double averageRate(){
    return reviews.stream().mapToInt(Review::getRate).average().getAsDouble(); //평균 계산
  }

  public int reviewCount(){
    return reviews.size();
  }


  /**
   * 연관관계 편의 메서드
   */
  public void addReview(){
  }

  public void addReviewTag(){
  }




}
