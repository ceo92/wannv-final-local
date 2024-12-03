package please_do_it.yumi;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import please_do_it.yumi.domain.Restaurant;
import please_do_it.yumi.dto.RestaurantSearchCond;
import please_do_it.yumi.dto.SearchBarDTO;
import please_do_it.yumi.service.RestaurantService;

@Controller
@RequiredArgsConstructor
public class HelloController {

  private final RestaurantService restaurantService;


  @RequestMapping
  public String hello(Model model) {
    //현재 위치 기준 , 전국 인기(높은 좋아요 and 높은 리뷰 수 and 높은 별점 ) , 가격대별 ,
    List<Restaurant> popularRestaurants = getPopularRestaurants();
    List<Restaurant> manyReviewsRestaurants = getManyReviewsRestaurants();
    List<Restaurant> highRatingRestaurants = getHighRatingRestaurants();
    List<Restaurant> manyLikesRestaurants = getManyLikesRestaurants();
    List<Restaurant> restaurantsByPriceRange1 = getRestaurantsByPriceRange(5000 , 10000);
    List<Restaurant> restaurantsByPriceRange2 = getRestaurantsByPriceRange(10000 , 30000);
    List<Restaurant> restaurantsByPriceRange3 = getRestaurantsByPriceRange(40000 , 60000);
    List<Restaurant> restaurantsByPriceRange4 = getRestaurantsByPriceRange(70000 , 100000);

    //메인에 보여지는 것들만 여기 정의하면 됨 ㅇㅇ

    model.addAttribute("popularRestaurants", popularRestaurants);
    model.addAttribute("manyReviewsRestaurants", manyReviewsRestaurants);
    model.addAttribute("highRatingRestaurants", highRatingRestaurants);
    model.addAttribute("manyLikesRestaurants", manyLikesRestaurants);
    model.addAttribute("restaurantsByPriceRange1", restaurantsByPriceRange1);
    model.addAttribute("restaurantsByPriceRange2", restaurantsByPriceRange2);
    model.addAttribute("restaurantsByPriceRange3", restaurantsByPriceRange3);
    model.addAttribute("restaurantsByPriceRange4", restaurantsByPriceRange4);

    return "index";
  }

  //그냥 정렬도 다중 조건으로 처리하자 다중 정렬 될 수 있게끔 하자 !
  /**
   * 현재 위치 기준 인기 식당 , 일단 보류 ㅇㅇ
   */
  private List<Restaurant> getPopularRestaurantsByCurrentLocation(String currentLocationRoadAddress) {

    RestaurantSearchCond currentLocationRestaurantCond = new RestaurantSearchCond();
    currentLocationRestaurantCond.setRoadAddress(currentLocationRoadAddress);
    currentLocationRestaurantCond.setSortConditions(Arrays.asList("LIKE" , "RATE" , "REVIEW"));
    List<Restaurant> popularRestaurants = restaurantService.findRestaurants(currentLocationRestaurantCond , null)
        .stream().limit(8).toList();
    return popularRestaurants;
  }

  /**
   * 전국 인기 식당
   */
  private List<Restaurant> getPopularRestaurants() {

    RestaurantSearchCond popularRestaurantCond = new RestaurantSearchCond();
    List<String> popularConditions = Arrays.asList("RATE", "LIKE", "REVIEW");
    popularRestaurantCond.setSortConditions(popularConditions);
    List<Restaurant> popularRestaurants = restaurantService.findRestaurants(popularRestaurantCond, null)
        .stream().limit(8).toList();
    return popularRestaurants;
  }

  /**
   * 좋아요 수 많은 추천 식당
   */
  private List<Restaurant> getManyLikesRestaurants() {
    RestaurantSearchCond manyLikesRestaurantCond = new RestaurantSearchCond();
    List<String> manyLikesConditions = List.of("LIKE");
    manyLikesRestaurantCond.setSortConditions(manyLikesConditions);
    return restaurantService.findRestaurants(manyLikesRestaurantCond , null).stream().limit(8).toList();
  }

  /**
   * 리뷰 수 많은 추천 식당
   */
  private List<Restaurant> getManyReviewsRestaurants() {
    RestaurantSearchCond manyReviewsRestaurantCond = new RestaurantSearchCond();
    List<String> manyReviewsConditions = List.of("REVIEW");
    manyReviewsRestaurantCond.setSortConditions(manyReviewsConditions);
    return restaurantService.findRestaurants(manyReviewsRestaurantCond , null).stream().limit(8).toList();
  }

  /**
   * 평점 높은 추천 식당
   */
  private List<Restaurant> getHighRatingRestaurants() {
    RestaurantSearchCond highRatingRestaurantCond = new RestaurantSearchCond();
    List<String> highRatingConditions = List.of("RATE");
    highRatingRestaurantCond.setSortConditions(highRatingConditions);
    return restaurantService.findRestaurants(highRatingRestaurantCond , null).stream().limit(8).toList();
  }

  /**
   * 가격대 별 추천 식당
   */
  private List<Restaurant> getRestaurantsByPriceRange(Integer startPrice , Integer endPrice) {
    RestaurantSearchCond priceRangeRestaurantCond = new RestaurantSearchCond();
    priceRangeRestaurantCond.setStartPrice(startPrice);
    priceRangeRestaurantCond.setEndPrice(endPrice);
    priceRangeRestaurantCond.setSortConditions(Arrays.asList("RATE" , "LIKE" , "REVIEW"));
    return restaurantService.findRestaurants(priceRangeRestaurantCond , null).stream().limit(8).toList();
  }


  /**
   * 분위기 좋은 식당들
   */
 /* private List<Restaurant> getRestaurantsByGreatMood() {
    RestaurantSearchCond greatMoodRestaurantCond = new RestaurantSearchCond();
    greatMoodRestaurantCond.setMoodTypes(Set.of(""));
    greatMoodRestaurantCond.setSortConditions(Arrays.asList("RATE" , "LIKE" , "REVIEW"));
    return restaurantService.findRestaurants(greatMoodRestaurantCond).stream().limit(8).toList();
  }*/











}
