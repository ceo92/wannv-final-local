package please_do_it.yumi.controller;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import please_do_it.yumi.constant.ContainFoodType;
import please_do_it.yumi.constant.MoodType;
import please_do_it.yumi.constant.ProvideServiceType;
import please_do_it.yumi.constant.ReservationTimeGap;
import please_do_it.yumi.constant.RestaurantType;
import please_do_it.yumi.domain.Restaurant;
import please_do_it.yumi.domain.Review;
import please_do_it.yumi.dto.RestaurantSaveDto;
import please_do_it.yumi.dto.RestaurantSearchCond;
import please_do_it.yumi.service.RestaurantService;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RestaurantController {


  private final RestaurantService restaurantService;

  @ModelAttribute("containFoodTypes")
  public ContainFoodType[] containFoodTypes() {
    return ContainFoodType.values();
  }
  
  @ModelAttribute("provideServiceTypes")
  public ProvideServiceType[] provideServiceTypes() {
    return ProvideServiceType.values();
  }

  @ModelAttribute("restaurantTypes")
  public RestaurantType[] restaurantTypes() {
    return RestaurantType.values();
  }

  @ModelAttribute("moodTypes")
  public MoodType[] moodTypes() {
    return MoodType.values();
  }


  @ModelAttribute("sortConditions")
  public Map<String, String> sortConditions() {
    Map<String, String> sortConditions = new HashMap<>();
    sortConditions.put("NEW", "최신 순");
    sortConditions.put("RATE", "평점 순");
    sortConditions.put("LIKE", "좋아요 순");
    sortConditions.put("REVIEW", "리뷰 순");
    return sortConditions;
  }


  @ModelAttribute("reservationTimeGaps")
  public ReservationTimeGap[] reservationGaps(){
    return ReservationTimeGap.values();
  }


  @ModelAttribute("dayOfWeeks")
  public List<String> dayOfWeeks(){
    List<String> dayOfWeeks = new ArrayList<>();
    dayOfWeeks.add("월요일");
    dayOfWeeks.add("화요일");
    dayOfWeeks.add("수요일");
    dayOfWeeks.add("목요일");
    dayOfWeeks.add("금요일");
    dayOfWeeks.add("토요일");
    dayOfWeeks.add("일요일");
    return dayOfWeeks;
  }

 /* @ModelAttribute("isDayOffList")
  public List<String> isDayOffList(){
    List<String> list = new ArrayList<>();
    list.add("월요일 휴무");
    list.add("화요일 휴무");
    list.add("수요일 휴무");
    list.add("월요일 휴무");
    list.add("월요일 휴무");
    list.add("월요일 휴무");
    list.add("월요일 휴무");


  }*/




  //restaurant
  @GetMapping("restaurants")
  public String getRestaurants(@ModelAttribute("restaurantSearchCond") RestaurantSearchCond restaurantSearchCond,
      Model model) {
    List<Restaurant> restaurants = restaurantService.findRestaurants(restaurantSearchCond);
    model.addAttribute("restaurants", restaurants);
    return "restaurant/restaurants";
  }

  @GetMapping("restaurants/{id}")
  public String getRestaurant(@PathVariable Long id, Model model) {
    System.out.println("id = " + id);
    Restaurant restaurant = restaurantService.findOne(id);
    String[] restaurantImages = restaurant.getRestaurantImages();
    for (String restaurantImage : restaurantImages) {
      System.out.println("restaurantImage = " + restaurantImage);
    }
    model.addAttribute("restaurant", restaurant);
    model.addAttribute("todayBusinessDay", restaurantService.findToday(restaurant));
    List<Restaurant> similarRestaurants = restaurantService.findSimilarRestaurants(id);
    model.addAttribute("similarRestaurants", similarRestaurants);
    model.addAttribute("reviewsByRating", getReviewsByRating(id));

    return "restaurant/restaurant";
  }


  @GetMapping("/restaurants/save")
  public String saveRestaurant(Model model){
    model.addAttribute("restaurantSaveDto", new RestaurantSaveDto());
    return "restaurant/admin-saveForm";
  }

  @PostMapping("/restaurants/save")
  public String saveRestaurantPost(@ModelAttribute("restaurantSaveDto") RestaurantSaveDto restaurantSaveDto ,  RedirectAttributes redirectAttributes){
    log.info("openTime = {}" , restaurantSaveDto.getOpenTimes());
    log.info("closeTime = {}" , restaurantSaveDto.getCloseTimes());
    log.info("breakStartTime = {}" , restaurantSaveDto.getBreakStartTimes());
    log.info("breakEndTime = {}" , restaurantSaveDto.getBreakEndTimes());
    log.info("lastOrder = {}" , restaurantSaveDto.getLastOrderTimes());
    log.info("isDayOff = {}", restaurantSaveDto.getIsDayOffList());



    /*if (bindingResult.hasErrors()){
      return "restaurant/admin-saveForm";
    }*/
    Long saveId = restaurantService.save(restaurantSaveDto);
    redirectAttributes.addAttribute("saveId" , saveId);
    return "redirect:/admin/restaurants/{saveId}";
  }

  @GetMapping("admin/restaurants/{id}")
  public String getAdminRestaurant(@PathVariable("id") Long id, Model model) {
    model.addAttribute("restaurant" ,restaurantService.findOne(id));
    return "restaurant/admin-restaurant";
  }

  /*@GetMapping("admin/restaurants")
  public String getAdminRestaurants(){
    restaurantService.findAdminRestaurants();
  }*/

  private Map<Integer, List<Review>> getReviewsByRating(Long id) {
    List<Review> reviewsByOneRating = restaurantService.findReviewsByRating(id, 1);
    List<Review> reviewsByTwoRating = restaurantService.findReviewsByRating(id, 2);
    List<Review> reviewsByThreeRating = restaurantService.findReviewsByRating(id, 3);
    List<Review> reviewsByFourRating = restaurantService.findReviewsByRating(id, 4);
    List<Review> reviewsByFiveRating = restaurantService.findReviewsByRating(id, 5);
    Map<Integer, List<Review>> reviewsByRating = new HashMap<>();
    reviewsByRating.put(1, reviewsByOneRating);
    reviewsByRating.put(2, reviewsByTwoRating);
    reviewsByRating.put(3, reviewsByThreeRating);
    reviewsByRating.put(4, reviewsByFourRating);
    reviewsByRating.put(5, reviewsByFiveRating);
    return reviewsByRating;
  }






  //UrlResource 자체가 필요 없음 , 어차피 Url직접 웹에서 링크로 조회해서 띄우는 것임 ㅇㅇ 내 서버로 들어와서 DB에 접근해서 띄우는 게 아닌 !


}
