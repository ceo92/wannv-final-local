package please_do_it.yumi.repository;

import static please_do_it.yumi.domain.QBusinessDay.businessDay;
import static please_do_it.yumi.domain.QFood.food;
import static please_do_it.yumi.domain.QLikes.likes;
import static please_do_it.yumi.domain.QRestaurant.restaurant;
import static please_do_it.yumi.domain.QReview.review;
import static please_do_it.yumi.domain.QReviewTag.reviewTag;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import please_do_it.yumi.constant.BusinessStatus;

import please_do_it.yumi.domain.Restaurant;
import please_do_it.yumi.dto.RestaurantResponseDto;
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


  public List<Restaurant> findAllReal(RestaurantSearchCond restaurantSearchCond){

    Boolean canPark = restaurantSearchCond.getCanPark();
    Boolean isOpen = restaurantSearchCond.getIsOpen();
    Integer startPrice = restaurantSearchCond.getStartPrice();
    Integer endPrice = restaurantSearchCond.getEndPrice();
    String roadAddress = restaurantSearchCond.getRoadAddress();
    List<Integer> rates = restaurantSearchCond.getRates();
    Set<String> restaurantTypes = restaurantSearchCond.getRestaurantTypes();
    Set<String> containFoodTypes = restaurantSearchCond.getContainFoodTypes();
    Set<String> provideServiceTypes = restaurantSearchCond.getProvideServiceTypes();
    Set<String> moodTypes = restaurantSearchCond.getMoodTypes();

    JPAQuery<Restaurant> dynamicQuery = query.selectFrom(restaurant)
        .join(restaurant.reviews, review)
        .join(restaurant.foods, food)
        .join(restaurant.likes , likes)
        .where(eqContainFoodTypes(containFoodTypes), eqRestaurantTypes(restaurantTypes),
            eqProvideServiceTypes(provideServiceTypes), eqMoodTypes(moodTypes),
            eqCanPark(canPark), eqIsOpen(isOpen), likeRoadAddress(roadAddress))
        .groupBy(restaurant) //restaurant.id로 해도 되고 restaurant로 해도 되는듯 ㅇㅇ 그냥 restaurant로 그루핑이 됨 ㅇㅇ
        .having(goeRate(rates), loeGoePrice(startPrice, endPrice));

    if (Boolean.TRUE.equals(restaurantSearchCond.getIsLikesChecked())){
      dynamicQuery.orderBy(likes.count().desc().nullsLast()); //좋아요 많은 순
    }
    if (Boolean.TRUE.equals(restaurantSearchCond.getIsReviewCountChecked())){
      dynamicQuery.orderBy(review.count().desc().nullsLast()); //리뷰 많은 순
    }
    if (Boolean.TRUE.equals(restaurantSearchCond.getIsCreatedAtChecked())){
      dynamicQuery.orderBy(restaurant.createdAt.desc().nullsLast()); //최신 순
    }
    if (Boolean.TRUE.equals(restaurantSearchCond.getIsRateChecked())){
      dynamicQuery.orderBy(review.rating.avg().desc().nullsLast()); //별점 높은 순
    }

    return dynamicQuery.fetch();
  }

  //모달 보안성 우수 => 제3자가 접근하기 어려움, 단순 팝업창 느낌이므로 , 이런 2가지 방법을 고려했을 때 ~~가 더 괜찮아서 이거를 선정하였다. 이렇게 면접이든 포폴이든 정의하자!
 public List<Restaurant> findAll(RestaurantSearchCond restaurantSearchCond) {
    /**
     * 지옥에서 온 쿼리
     */
    Boolean canPark = restaurantSearchCond.getCanPark();
    Boolean isOpen = restaurantSearchCond.getIsOpen();
    Integer startPrice = restaurantSearchCond.getStartPrice();
    Integer endPrice = restaurantSearchCond.getEndPrice();
    String roadAddress = restaurantSearchCond.getRoadAddress();
    List<Integer> rates = restaurantSearchCond.getRates();
    Set<String> restaurantTypes = restaurantSearchCond.getRestaurantTypes();
    Set<String> containFoodTypes = restaurantSearchCond.getContainFoodTypes();
    Set<String> provideServiceTypes = restaurantSearchCond.getProvideServiceTypes();
    Set<String> moodTypes = restaurantSearchCond.getMoodTypes();

   JPAQuery<Restaurant> dynamicQuery = query.select(restaurant)
       .from(restaurant)
       .join(restaurant.reviews, review)
       .join(restaurant.foods, food)
       .join(restaurant.businessDays, businessDay)
       .join(restaurant.likes, likes)
       .join(review.reviewTags, reviewTag)
       .where(eqContainFoodTypes(containFoodTypes), eqRestaurantTypes(restaurantTypes),
           eqProvideServiceTypes(provideServiceTypes), eqMoodTypes(moodTypes),
           loeGoePrice(startPrice, endPrice),
           eqCanPark(canPark), eqIsOpen(isOpen), likeRoadAddress(roadAddress))
       .groupBy(restaurant.id)
       .having(goeRate(rates)); //이거부터 해결 ㄱㄱ
   //여기까진 픽스 11/24 오후 3:17

   /*if (Boolean.TRUE.equals(restaurantSearchCond.getIsLikesChecked())){
     dynamicQuery.orderBy(likes.count().desc().nullsLast()); //좋아요 많은 순
   }
   if (Boolean.TRUE.equals(restaurantSearchCond.getIsReviewCountChecked())){
     dynamicQuery.orderBy(review.count().desc().nullsLast()); //리뷰 많은 순
   }
   if (Boolean.TRUE.equals(restaurantSearchCond.getIsCreatedAtChecked())){
     dynamicQuery.orderBy(restaurant.createdAt.desc().nullsLast()); //최신 순
   }
   if (Boolean.TRUE.equals(restaurantSearchCond.getIsRateChecked())){
     dynamicQuery.orderBy(review.rating.avg().desc().nullsLast()); //별점 높은 순
   }
*/
   // 이 order by 정렬 조건들은 파라메터가 필요 없음 파라메터는 그냥 검증 역할 정도 해주는 거고 파라메터가 orderBy 내부에 들어가지 않음 , 즉 응답할 일이 없음
   if (restaurantSearchCond.getIsLikesChecked().equals(true)){
     dynamicQuery.orderBy(likes.count().desc().nullsLast()); //좋아요 많은 순
   }
   if (restaurantSearchCond.getIsReviewCountChecked().equals(true)){
     dynamicQuery.orderBy(review.count().desc().nullsLast()); //리뷰 많은 순
   }
   if (restaurantSearchCond.getIsCreatedAtChecked().equals(true)){
     dynamicQuery.orderBy(restaurant.createdAt.desc().nullsLast()); //최신 순
   }
   if (restaurantSearchCond.getIsRateChecked().equals(true)){
     dynamicQuery.orderBy(review.rating.avg().desc().nullsLast()); //별점 높은 순
   }

   return dynamicQuery.fetch();
  }


  private BooleanExpression eqContainFoodTypes(Set<String> containFoodTypes) {
    if (containFoodTypes == null || containFoodTypes.isEmpty()) {
      return null; // 조건이 없으면 null 반환
    }
    BooleanExpression booleanExpression = null;
    for (String containFoodType : containFoodTypes) {
      if (containFoodType != null) {
        BooleanExpression condition = restaurant.containFoodTypes.any().eq(containFoodType);
        booleanExpression = (booleanExpression == null) ? condition : booleanExpression.or(condition); //or 조건을 꼭 후미에 붙여줘야함
      }
    }
    return booleanExpression;
  }




  private BooleanExpression eqRestaurantTypes(Set<String> restaurantTypes) {
    if (restaurantTypes == null || restaurantTypes.isEmpty()) {
      return null; // 조건이 없으면 null 반환
    }
    BooleanExpression booleanExpression = null;
    for (String restaurantType : restaurantTypes) {
      if (restaurantType != null) {
        BooleanExpression condition = restaurant.restaurantTypes.any().eq(restaurantType);
        booleanExpression = (booleanExpression == null) ? condition : booleanExpression.or(condition); //or 조건을 꼭 후미에 붙여줘야함
      }
    }
    return booleanExpression;
  }



  private BooleanExpression eqProvideServiceTypes(Set<String> provideServiceTypes) {
    if (provideServiceTypes == null || provideServiceTypes.isEmpty()) {
      return null; // 조건이 없으면 null 반환
    }

    BooleanExpression booleanExpression = null;
    for (String provideServiceType : provideServiceTypes) {
      if (provideServiceType != null) {
        BooleanExpression condition = restaurant.provideServiceTypes.any().eq(provideServiceType);
        booleanExpression =
            (booleanExpression == null) ? condition : booleanExpression.or(condition); //or 조건을 꼭 후미에 붙여줘야함
      }
    }
    return booleanExpression;
  }

  private BooleanExpression eqMoodTypes(Set<String> moodTypes){
    if (moodTypes == null || moodTypes.isEmpty()) {
      return null; // 조건이 없으면 null 반환
    }

    BooleanExpression booleanExpression = null;
    for (String moodType : moodTypes) {
      if (moodType != null) {
        BooleanExpression condition = restaurant.moodTypes.any().eq(moodType);
        booleanExpression = (booleanExpression == null) ? condition : booleanExpression.or(condition);
      }
    }

    return booleanExpression;
  }


  //goeRate : 별표 1,2,3,4,5 체크박스 중 여러 개를 누를 수 있어서 List를 받아올 수 있는 것이고,  이때 별점은 평균별점임 그래서 avg로 계산했음
  private BooleanExpression goeRate(List<Integer> rates) {
    if (rates == null || rates.isEmpty()) {
      return null; // 조건이 없으면 null 반환
    }

    BooleanExpression booleanExpression = null;
    for (Integer rate : rates) {
      if (rate != null) {
        BooleanExpression condition = review.rating.avg().goe(rate).and(review.rating.avg().lt(rate + 1));
        booleanExpression = (booleanExpression == null) ? condition : booleanExpression.or(condition);
      }
    }

    return booleanExpression;
  }

  private BooleanExpression loeGoePrice(Integer startPrice, Integer endPrice) {
    return startPrice != null && endPrice != null ? food.price.avg().goe(startPrice).and(food.price.avg().loe(endPrice)) : null;
  }


  private BooleanExpression likeRoadAddress(String roadAddress){
    return StringUtils.hasText(roadAddress) ? restaurant.address.roadAddress.like(roadAddress) : null;
  }

  private BooleanExpression eqIsOpen(Boolean isOpen) {
    return Boolean.TRUE.equals(isOpen) ? restaurant.businessStatus.eq(BusinessStatus.OPEN) : null; //영업 중인지 판별 , 누군가 체크박스에 영업 중 여부를 체크했을 경우 영업 중만 뜨게끔 조건 추가하는 것!
  }

  private BooleanExpression eqCanPark(Boolean canPark) {
    return Boolean.TRUE.equals(canPark) ? restaurant.canPark.eq(true) : null;
  }





}
