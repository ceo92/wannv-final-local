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

  public List<Restaurant> findAll(){
    return query.selectFrom(restaurant)
        .join(restaurant.reviews , review)
        .join(restaurant.foods, food)
        .join(restaurant.businessDays, businessDay)
        .join(review.reviewTags, reviewTag)
        .fetch();
  }

  public List<Tuple> findAllWithTuple(){
    return query
        .select(restaurant, review , food , businessDay)
        .from(restaurant)
        .join(restaurant.reviews, review) //조인으로 가져와야 오류가 안 나지
        .join(restaurant.foods , food)
        .join(restaurant.businessDays , businessDay)
        .join(review.reviewTags , reviewTag)
        .fetch();
  }


  public List<Restaurant> findAllOnlyRestaurant(){
    return query
        .select(restaurant)
        .from(restaurant)
        .join(restaurant.reviews, review) //조인으로 가져와야 오류가 안 나지
        .join(restaurant.foods , food)
        .join(restaurant.businessDays , businessDay)
        .join(review.reviewTags , reviewTag)
        .fetch();
  }




  //모달 보안성 우수 => 제3자가 접근하기 어려움, 단순 팝업창 느낌이므로 , 이런 2가지 방법을 고려했을 때 ~~가 더 괜찮아서 이거를 선정하였다. 이렇게 면접이든 포폴이든 정의하자!
  public List<Restaurant> findAll(RestaurantSearchCond restaurantSearchCond) {
    /**
     * where 동적 조건
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

//원래 .join( 조인할 연관관계 , 연관관계에 대한 Q타입)
    JPAQuery<Restaurant> dynamicQuery = query.selectFrom(restaurant)
        .join(restaurant.reviews, review)
        .join(restaurant.foods, food)
        .join(restaurant.businessDays, businessDay)
        .join(restaurant.likes, likes)
        .join(review.reviewTags, reviewTag)
        .where(eqContainFoodTypes(containFoodTypes), goeRate(rates),
            eqRestaurantTypes(restaurantTypes),
            eqProvideServiceTypes(provideServiceTypes), eqMoodTypes(moodTypes)
            , loeGoePrice(startPrice, endPrice),
            eqCanPark(canPark), eqIsOpen(isOpen), eqRoadAddress(roadAddress));
    //최신 순(등록일 기준 정렬) , 별점 높은 순(평균 별점에 따른 정렬) , 좋아요 높은 순
    /*if (restaurantSearchCond.getIsLikesChecked()){
      dynamicQuery.groupBy(likes.count()).orderBy(likes.count().desc().nullsLast()); //좋아요 많은 순
    }
    if (restaurantSearchCond.getIsReviewCountChecked()){
      dynamicQuery.groupBy(review.count()).orderBy(review.count().desc().nullsLast()); //리뷰 많은 순
    }
    if (restaurantSearchCond.getIsCreatedAtChecked()){
      dynamicQuery.groupBy(restaurant.createdAt).orderBy(restaurant.createdAt.desc().nullsLast()); //최신 순
    }
    if (restaurantSearchCond.getIsRateChecked()){
      dynamicQuery.groupBy(review.rating.count()).orderBy(review.rating.count().desc().nullsLast()); //별점 높은 순
    }
*/
    return dynamicQuery.fetch();



    //코드가 굉장히 간결 , return 문만 봐도 동적 쿼리 짤 수 있음
  }




  private BooleanExpression eqContainFoodTypes(Set<String> containFoodTypes){
    BooleanExpression booleanExpression = null;
    for (String containFoodType : containFoodTypes) {
      booleanExpression =  containFoodType != null ? restaurant.containFoodTypes.any().eq(containFoodType) : null;
    }
    return booleanExpression;
  }

  private BooleanExpression eqRestaurantTypes(Set<String> restaurantTypes){
    BooleanExpression booleanExpression = null;
    for (String restaurantType : restaurantTypes) {
      booleanExpression =  restaurantType != null ? restaurant.restaurantTypes.any().eq(restaurantType) : null;
    }
    return booleanExpression;
  }

  private BooleanExpression eqProvideServiceTypes(Set<String> provideServiceTypes){
    BooleanExpression booleanExpression = null;
    for (String provideServiceType : provideServiceTypes) {
      booleanExpression =  provideServiceType != null ? restaurant.provideServiceTypes.any().eq(provideServiceType) : null;
    }
    return booleanExpression;
  }

  private BooleanExpression eqMoodTypes(Set<String> moodTypes){
    BooleanExpression booleanExpression = null;
    for (String moodType : moodTypes) {
      booleanExpression =  moodType != null ? restaurant.moodTypes.any().eq(moodType) : null;
    }
    return booleanExpression;
  }

  private BooleanExpression goeRate(List<Integer> rates) {
    BooleanExpression booleanExpression = null;
    for (Integer rate : rates) {
      booleanExpression = rate != null ? review.rating.avg().goe(rate).and(review.rating.lt(rate+1)) : null;
    }
    return booleanExpression;

  }

  private BooleanExpression eqRoadAddress(String roadAddress){
    return StringUtils.hasText(roadAddress) ? restaurant.address.roadAddress.eq(roadAddress) : null;
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




}
