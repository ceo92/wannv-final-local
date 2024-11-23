package please_do_it.yumi.repository;

import static please_do_it.yumi.domain.QRestaurant.restaurant;
import static please_do_it.yumi.domain.QReview.review;

import com.querydsl.core.Tuple;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import please_do_it.yumi.domain.Restaurant;
import please_do_it.yumi.domain.Review;
import please_do_it.yumi.domain.ReviewTag;
import please_do_it.yumi.dto.RestaurantSearchCond;

@SpringBootTest
@Transactional
class RestaurantRepositoryTest {

  @Autowired
  RestaurantRepository restaurantRepository;

  @Test
  void groupby_having_test(){
    Set<String> containFoodTypes = new HashSet<>();
    containFoodTypes.add("계란 포함");
    containFoodTypes.add("동물의 알 포함");

    Set<String> restaurantTypes = new HashSet<>();
    restaurantTypes.add("일식");
    restaurantTypes.add("중식");

    Set<String> moodTypes = new HashSet<>();
    moodTypes.add("따뜻한");
    moodTypes.add("안락한");

    Set<String> provideServiceTypes = new HashSet<>();
    provideServiceTypes.add("배달 가능");
    provideServiceTypes.add("포장 가능");


    List<Restaurant> restaurants = restaurantRepository.findAll(
        new RestaurantSearchCond(10000, 20000, true, true,
            "도%", Arrays.asList(3,4,5), containFoodTypes,restaurantTypes ,
            provideServiceTypes, moodTypes , true,
            true, true, true));

    for (Restaurant restaurant : restaurants) {
      System.out.println("restaurant = " + restaurant);
      System.out.println("restaurant.getReviews() = " + restaurant.getReviews());
      System.out.println("restaurant.getFoods() = " + restaurant.getFoods());
      System.out.println("restaurant.getBusinessDays() = " + restaurant.getBusinessDays());

      List<Review> reviews = restaurant.getReviews();

      for (Review review1 : reviews) {
        List<ReviewTag> reviewTags = review1.getReviewTags();
        for (ReviewTag reviewTag : reviewTags) {
          System.out.println("reviewTag.get = " + reviewTag.getTag());
        }

      }




    }
  }


  @Test
  void select_from_join_where만으로조회() {
    Set<String> containFoodTypes = new HashSet<>();
    containFoodTypes.add("계란 포함");
    containFoodTypes.add("동물의 알 포함");

    Set<String> restaurantTypes = new HashSet<>();
    restaurantTypes.add("일식");
    restaurantTypes.add("중식");

    Set<String> moodTypes = new HashSet<>();
    moodTypes.add("따뜻한");
    moodTypes.add("안락한");

    Set<String> provideServiceTypes = new HashSet<>();
    provideServiceTypes.add("배달 가능");
    provideServiceTypes.add("포장 가능");


  }



}