package wannav.local.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wannav.local.controller.RestaurantController;
import wannav.local.domain.Restaurant;
import wannav.local.dto.RestaurantSearchCond;

@SpringBootTest
@Transactional
class RestaurantRepositoryTest {

  @Autowired
  RestaurantRepository restaurantRepository;

  @Autowired
  RestaurantController restaurantController;



  @Test
  void 식당전체조회_지역설정_테스트(){
    //given
    RestaurantSearchCond restaurantSearchCond = new RestaurantSearchCond();
    restaurantSearchCond.setRoadAddress("서울 강남구 역삼로");

    //when
    List<Restaurant> results = restaurantRepository.findAll(restaurantSearchCond, null);

    //then
    Assertions.assertThat(results.size()).isEqualTo(9);
  }


/*


  @Test
  void ds(){
    RestaurantSearchCond restaurantSearchCond = new RestaurantSearchCond();
    ArrayList<Integer> rates = new ArrayList<>();
    rates.add(1);
    rates.add(2);
    rates.add(3);
    rates.add(4); //4까지만 있게 해야되네 ㅇㅇ , 애초에 5점은 말이 안되지 별점 평균이 ㅋㅋ

    Set<String> containFoodTypes = new HashSet<>();
    containFoodTypes.add("계란 포함");
    containFoodTypes.add("유제품 포함");
    containFoodTypes.add("생선 포함");

    Set<String> restaurantTypes = new HashSet<>();
    restaurantTypes.add("한식");
    restaurantTypes.add("이탈리아 음식");
    restaurantTypes.add("터키 음식");

    Set<String> provideServiceTypes = new HashSet<>();
    provideServiceTypes.add("예약 가능");
    provideServiceTypes.add("포장 가능");
    provideServiceTypes.add("배달 가능");

    Set<String> moodTypes = new HashSet<>();
    moodTypes.add("데이트하기 좋은");
    moodTypes.add("활기찬");


*/
/*
    restaurantSearchCond.setStartPrice(20000);
    restaurantSearchCond.setEndPrice(30000);
    restaurantSearchCond.setRoadAddress("서울%");
*//*


    List<String> sortConditions = restaurantSearchCond.getSortConditions();
    sortConditions.add("NEW");
    sortConditions.add("REVIEW");
    sortConditions.add("RATE");
    sortConditions.add("LIKE");
    List<Restaurant> all = restaurantRepository.findAll(restaurantSearchCond);
    for (Restaurant restaurant1 : all) {
      System.out.println("restaurant = " + restaurant1);
      */
/*List<Review> reviews = restaurant1.getReviews();
      List<Food> foods = restaurant1.getFoods();
      for (Review review1 : reviews) {
        System.out.println("review = " + review1);
      }
      System.out.println("음식들의 평균 값 : "+foods.stream().mapToInt(a->a.getPrice()).average().getAsDouble());
      for (Food food : foods) {
        System.out.println("food = " + food);
      }*//*

    }
  }
*/


  @Test
  void sfjfsdjk(){
    RestaurantSearchCond restaurantSearchCond = new RestaurantSearchCond();
    List<Restaurant> all = restaurantRepository.findAll(restaurantSearchCond, "음");
    for (Restaurant restaurant : all) {
      System.out.println("restaurant = " + restaurant);
    }

  }




}