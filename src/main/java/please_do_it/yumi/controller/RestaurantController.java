package please_do_it.yumi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import please_do_it.yumi.constant.ContainFoodType;
import please_do_it.yumi.constant.MoodType;
import please_do_it.yumi.constant.ProvideServiceType;
import please_do_it.yumi.constant.ReservationTimeGap;
import please_do_it.yumi.constant.RestaurantType;
import please_do_it.yumi.domain.BusinessDay;
import please_do_it.yumi.domain.Restaurant;
import please_do_it.yumi.domain.Review;
import please_do_it.yumi.dto.*;
import please_do_it.yumi.service.FileService;
import please_do_it.yumi.service.RestaurantService;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RestaurantController {

  @Value("${restaurant.image.dir}")
  private String restaurantDir;

  @Value("${food.image.dir}")
  private String foodDir;

  private final RestaurantService restaurantService;
  private final FileService fileService;

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
  public ReservationTimeGap[] reservationGaps() {
    return ReservationTimeGap.values();
  }


  @ModelAttribute("dayOfWeeks")
  public List<String> dayOfWeeks() {
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


  @GetMapping("/admin-restaurants/save")
  public String saveRestaurant(Model model) {
    model.addAttribute("restaurantSaveDto", new RestaurantSaveDto());
    return "restaurant/admin-saveForm";
  }

  @PostMapping("/admin-restaurants/save")
  @ResponseBody
  public String saveRestaurantPost(@ModelAttribute("restaurantSaveDto") RestaurantSaveDto restaurantSaveDto, RedirectAttributes redirectAttributes) {
    log.info("restaurant = {}" , restaurantSaveDto.getRestaurantImages());
    log.info("food = {}", restaurantSaveDto.getFoodSaveDtoList());


    List<MultipartFile> restaurantImages = restaurantSaveDto.getRestaurantImages();
    List<MultipartFile> foodImages = new ArrayList<>();
    List<FoodSaveDto> foodSaveDtoList = restaurantSaveDto.getFoodSaveDtoList();
    for (FoodSaveDto foodSaveDto : foodSaveDtoList) {
      foodImages.add(foodSaveDto.getFoodImage());
    }

    /**
     * 식당 스토리지 저장 + DB에 스토리지 Url 저장
     */
    List<FileDTO> restaurantImagesFileDto = fileService.uploadFiles(restaurantImages, restaurantDir);
    List<String> restaurantImagesUrl = restaurantImagesFileDto.stream().map(FileDTO::getUploadFileUrl).toList();
    restaurantSaveDto.setRestaurantImagesUrl(restaurantImagesUrl);

    /**
     * 음식 스토리지 저장 + DB에 스토리지 Url 저장
     */
    List<FileDTO> foodImagesFileDto = fileService.uploadFiles(foodImages, foodDir);
    List<String> foodImagesUrl = foodImagesFileDto.stream().map(FileDTO::getUploadFileUrl).toList();
    for (String foodImageUrl : foodImagesUrl) {
      foodSaveDtoList.forEach(foodSaveDto -> {
        foodSaveDto.setFoodImageUrl(foodImageUrl);
      });
    };

    log.info("restaurantSaveDto = {}" , restaurantSaveDto);
    log.info("foodSaveDtoList = {}" , restaurantSaveDto.getFoodSaveDtoList());

    Long saveId = restaurantService.save(restaurantSaveDto);
    redirectAttributes.addAttribute("saveId", saveId);
    return "success";
  }


  @GetMapping("/admin-restaurants/{id}/update")
  public String updateRestaurant(@PathVariable("id") Long id ,  Model model) {
    Restaurant restaurant = restaurantService.findOne(id);
    RestaurantUpdateDto restaurantUpdateDto = new RestaurantUpdateDto(restaurant.getName() , restaurant.getBusinessNum() , restaurant.getRestaurantTypes() , restaurant.getContainFoodTypes() , restaurant.getProvideServiceTypes() , restaurant.getMoodTypes() , restaurant.getAddress().getRoadAddress() , restaurant.getAddress().getLandLotAddress() , restaurant.getAddress().getZipCode() , restaurant.getAddress().getDetailAddress() , restaurant.getCanPark() , restaurant.getReservationTimeGap() , restaurant.getIsPenalty());
    model.addAttribute("restaurant", restaurant);
    return "restaurant/admin-updateForm";
  }




  @GetMapping("admin-restaurants/{id}")
  public String getAdminRestaurant(@PathVariable("id") Long id, Model model) {
    model.addAttribute("restaurant", restaurantService.findOne(id));
    return "restaurant/admin-restaurant";
  }

  @GetMapping("admin-restaurants")
  public String getAdminRestaurants(@ModelAttribute("restaurantAdminSearchCond") RestaurantAdminSearchCond restaurantAdminSearchCond , Model model) {
    model.addAttribute("restaurants", restaurantService.findRestaurants(new RestaurantSearchCond()));
    return "restaurant/admin-restaurants";
  }

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
