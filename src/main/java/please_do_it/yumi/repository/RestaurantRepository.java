package please_do_it.yumi.repository;

import static please_do_it.yumi.domain.QBusinessDay.businessDay;
import static please_do_it.yumi.domain.QFood.food;
import static please_do_it.yumi.domain.QRestaurant.restaurant;
import static please_do_it.yumi.domain.QReview.review;
import static please_do_it.yumi.domain.QReviewTag.reviewTag;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import please_do_it.yumi.constant.BusinessStatus;
import please_do_it.yumi.domain.Restaurant;
import please_do_it.yumi.dto.RestaurantSearchCond;

@Repository
public class RestaurantRepository {

  private final EntityManager em;
  private final JPAQueryFactory query;


  @Autowired
  public RestaurantRepository(EntityManager em){
    this.em=em;
    this.query = new JPAQueryFactory(em);
  }

  public Long save(Restaurant restaurant){
    em.persist(restaurant);
    return restaurant.getId();
  }

  public Optional<Restaurant> findById(Long id){
    return Optional.ofNullable(em.find(Restaurant.class , id));
  }

  public List<Restaurant> findAll(RestaurantSearchCond restaurantSearchCond) {
    Integer rate = restaurantSearchCond.getRate();
    Boolean canPark = restaurantSearchCond.getCanPark();
    Boolean isOpen = restaurantSearchCond.getIsOpen();
    Integer startPrice = restaurantSearchCond.getStartPrice();
    Integer endPrice = restaurantSearchCond.getEndPrice();
    String roadAddress = restaurantSearchCond.getRoadAddress();
    List<String> restaurantTypes = restaurantSearchCond.getRestaurantTypes();
    List<String> containFoodTypes = restaurantSearchCond.getContainFoodTypes();
    List<String> provideServiceTypes = restaurantSearchCond.getProvideServiceTypes();
    List<String> moodTypes = restaurantSearchCond.getMoodTypes();

    return query.selectFrom(restaurant)
        .join(restaurant.reviews , review)
        .join(restaurant.foods , food)
        .join(restaurant.businessDays , businessDay)
        .join(review.reviewTags , reviewTag)
        .where(eqContainFoodTypes(containFoodTypes) , eqRestaurantTypes(restaurantTypes),
            eqProvideServiceTypes(provideServiceTypes), eqMoodTypes(moodTypes),
            goeRate(rate), loeGoePrice(startPrice , endPrice) ,
            eqCanPark(canPark) , eqIsOpen(isOpen) , eqRoadAddress(roadAddress))
        .fetch();



    //코드가 굉장히 간결 , return 문만 봐도 동적 쿼리 짤 수 있음
  }


  private BooleanExpression eqContainFoodTypes(List<String> containFoodTypes){
    BooleanExpression booleanExpression = null;
    for (String containFoodType : containFoodTypes) {
      booleanExpression =  containFoodType != null ? restaurant.containFoodTypes.any().eq(containFoodType) : null;
    }
    return booleanExpression;
  }

  private BooleanExpression eqRestaurantTypes(List<String> restaurantTypes){
    BooleanExpression booleanExpression = null;
    for (String restaurantType : restaurantTypes) {
      booleanExpression =  restaurantType != null ? restaurant.restaurantTypes.any().eq(restaurantType) : null;
    }
    return booleanExpression;
  }

  private BooleanExpression eqProvideServiceTypes(List<String> provideServiceTypes){
    BooleanExpression booleanExpression = null;
    for (String provideServiceType : provideServiceTypes) {
      booleanExpression =  provideServiceType != null ? restaurant.provideServiceTypes.any().eq(provideServiceType) : null;
    }
    return booleanExpression;
  }

  private BooleanExpression eqMoodTypes(List<String> moodTypes){
    BooleanExpression booleanExpression = null;
    for (String moodType : moodTypes) {
      booleanExpression =  moodType != null ? restaurant.moodTypes.any().eq(moodType) : null;
    }
    return booleanExpression;
  }

  private BooleanExpression eqRoadAddress(String roadAddress){
    return roadAddress != null ? restaurant.address.roadAddress.eq(roadAddress) : null;
  }

  private BooleanExpression eqIsOpen(Boolean isOpen) {
    return isOpen != null ? restaurant.businessStatus.eq(BusinessStatus.OPEN) : null; //영업 중인지 판별 , 누군가 체크박스에 영업 중 여부를 체크했을 경우 영업 중만 뜨게끔 조건 추가하는 것!
  }

  private BooleanExpression eqCanPark(Boolean canPark) {
    return canPark != null ? restaurant.canPark.eq(canPark) : null;
  }

  private BooleanExpression loeGoePrice(Integer startPrice, Integer endPrice) {
    return startPrice != null && endPrice != null ? food.price.loe(endPrice).and(food.price.goe(startPrice)) : null;
  }

  private BooleanExpression goeRate(Integer rate) {
    return rate != null ? review.rate.goe(rate).and(review.rate.loe(rate+1)) : null; //1. 평균 별점이니 계산 로직 들어가야됨


  }


}
