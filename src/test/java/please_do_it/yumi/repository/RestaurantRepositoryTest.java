package please_do_it.yumi.repository;

import static org.junit.jupiter.api.Assertions.*;
import static please_do_it.yumi.domain.QBusinessDay.businessDay;
import static please_do_it.yumi.domain.QFood.food;
import static please_do_it.yumi.domain.QRestaurant.restaurant;
import static please_do_it.yumi.domain.QReview.review;
import static please_do_it.yumi.domain.QReviewTag.reviewTag;

import com.querydsl.core.Tuple;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import please_do_it.yumi.domain.Restaurant;
import please_do_it.yumi.dto.RestaurantSearchCond;

@SpringBootTest
@Transactional
class RestaurantRepositoryTest {

  @Autowired
  RestaurantRepository restaurantRepository;

  @Rollback(value = false)
  @Test
  void save() {
    String nowDayOfWeek = LocalDate.now().getDayOfWeek()
        .getDisplayName(TextStyle.FULL, Locale.KOREAN); //요일 정보(월요일 , ... , 일요일 ) 담김
    System.out.println("nowDayOfWeek = " + nowDayOfWeek);
  }


  @Test
  void sdfjsdfjkfsd(){
    List<Tuple> allTuple = restaurantRepository.findAllTuple(
        new RestaurantSearchCond(10000, 20000, true, null, null, Collections.emptyList(),
            Collections.emptySet(), Collections.emptySet(), Collections.emptySet(),
            Collections.emptySet(), null,
            null, null, null));
    for (Tuple tuple : allTuple) {
      System.out.println("tuple.get(restaurant) = " + tuple.get(restaurant));
    }

  }

  @Test
  void findAllWithTuple() {

    List<Restaurant> tuples = restaurantRepository.findAll(
        new RestaurantSearchCond(10000, 20000, true, true, "도%", Collections.emptyList(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), null,
            null, null, null));
    for (Restaurant tuple : tuples) {
      System.out.println("tuple = " + tuple);
    }
    /*List<Tuple> tuples = restaurantRepository.findAllWithTuple();
    for (Tuple tuple : tuples) {
      System.out.println("restaurant = " + tuple.get(restaurant));
      System.out.println("review = " + tuple.get(review));
      System.out.println("food = " + tuple.get(food));
      System.out.println("businessDay = " + tuple.get(businessDay));
      System.out.println("reviewTag = " + tuple.get(reviewTag));
    }
    */
  }

  @Test
  void findAllOnlyRestaurant() {
    List<Restaurant> restaurants = restaurantRepository.findAllOnlyRestaurant();
    for (Restaurant restaurant1 : restaurants) {
      System.out.println("restaurant = " + restaurant1);
      System.out.println("restaurant.foods = " + restaurant1.getFoods());
      System.out.println("restaurant.businessDays = " + restaurant1.getBusinessDays());
      System.out.println("restaurant.reviews() = " + restaurant1.getReviews());

      System.out.println();
      System.out.println(
          "==========================================================================================");
      System.out.println(
          "==========================================================================================");
      System.out.println(
          "==========================================================================================");
      System.out.println();

    }
  }


}