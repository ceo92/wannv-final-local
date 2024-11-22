package please_do_it.yumi.repository;

import static please_do_it.yumi.domain.QRestaurant.restaurant;
import static please_do_it.yumi.domain.QReview.review;

import com.querydsl.core.Tuple;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
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


    List<Tuple> tuples = restaurantRepository.findAllTuple(
        new RestaurantSearchCond(10000, 20000, true, true,
            "도%", Arrays.asList(3,4,5), containFoodTypes,restaurantTypes ,
            provideServiceTypes, moodTypes , null,
            null, null, null));
    for (Tuple tuple : tuples) {
      System.out.println("tuple.get(restaurant) = " + tuple.get(restaurant.id));
      System.out.println("tuple.get(restaurant) = " + tuple.get(restaurant.name));
      System.out.println("tuple.get(restaurant) = " + tuple.get(review.rating.avg()));
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




    List<Restaurant> tuples = restaurantRepository.findAll(
        new RestaurantSearchCond(10000, 20000, true, true,
            "도%", Collections.emptyList(), containFoodTypes,restaurantTypes ,
            provideServiceTypes, moodTypes , null,
            null, null, null));
    for (Restaurant tuple : tuples) {
      System.out.println("tuple = " + tuple);
    }

  }



}