package wannav.local.controller;

import java.time.LocalTime;
import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wannav.local.constant.ContainFoodType;
import wannav.local.constant.MoodType;
import wannav.local.constant.ProvideServiceType;
import wannav.local.constant.ReservationTimeGap;
import wannav.local.constant.RestaurantType;
import wannav.local.domain.BusinessDay;
import wannav.local.domain.Food;
import wannav.local.domain.Restaurant;
import wannav.local.domain.Review;
import wannav.local.dto.*;
import wannav.local.service.FileService;
import wannav.local.service.RestaurantService;

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


  @ModelAttribute("adminSortConditions")
  public Map<String , String> adminSortConditions(){
    Map<String, String> adminSortConditions = new HashMap<>();
    adminSortConditions.put("NEW", "최신 순");
    adminSortConditions.put("REGISTER", "등록 순");
    return adminSortConditions;
  }


  /*@ModelAttribute("regionsSeoul")
  public List<String> regions(){
    List<String> regions = new ArrayList<>();
    regions.add("경기 수원시");

  }*/


  //restaurant
  @GetMapping("restaurants")
  public String getRestaurants(@ModelAttribute("restaurantSearchCond") RestaurantSearchCond restaurantSearchCond,
                               @RequestParam(value = "search" , required = false) String search,
                               Model model) {
    List<Restaurant> restaurants = restaurantService.findRestaurants(restaurantSearchCond, search);
    System.out.println("restaurants = " + restaurants);
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
  public String saveRestaurantPost(@ModelAttribute("restaurantSaveDto") RestaurantSaveDto restaurantSaveDto, RedirectAttributes redirectAttributes) {



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



    Long saveId = restaurantService.save(restaurantSaveDto);
    redirectAttributes.addAttribute("saveId", saveId);
    return "redirect:/admin-restaurants/{saveId}";
  }


  @GetMapping("/admin-restaurants/{id}/update")
  public String updateRestaurant(@PathVariable("id") Long id ,  Model model) {
    Restaurant restaurant = restaurantService.findOne(id);


     // Set Restaurant DTO

    RestaurantUpdateDto restaurantUpdateDto = RestaurantUpdateDto.builder().id(id).contact(restaurant.getContact()).description(restaurant.getDescription())
            .restaurantName(restaurant.getName()).restaurantTypes(restaurant.getRestaurantTypes())
            .restaurantImagesUrl(Arrays.asList(restaurant.getRestaurantImages())).businessNum(restaurant.getBusinessNum())
            .containFoodTypes(restaurant.getContainFoodTypes()).provideServiceTypes(restaurant.getProvideServiceTypes()).moodTypes(restaurant.getMoodTypes())
            .detailAddress(restaurant.getAddress().getDetailAddress()).landLotAddress(restaurant.getAddress().getLandLotAddress())
            .roadNameAddress(restaurant.getAddress().getRoadAddress()).canPark(restaurant.getCanPark()).isPenalty(restaurant.getIsPenalty())
            .reservationTimeGap(convertReservationTimeGapToString(restaurant.getReservationTimeGap())).build();


     // Set BusinessDay DTO

    List<BusinessDay> businessDays = restaurant.getBusinessDays();
    List<LocalTime> openTimes = businessDays.stream().map(BusinessDay::getOpenTime).toList();
    List<LocalTime> closeTimes = businessDays.stream().map(BusinessDay::getCloseTime).toList();
    List<LocalTime> breakStartTimes = businessDays.stream().map(BusinessDay::getBreakStartTime).toList();
    List<LocalTime> breakEndTimes = businessDays.stream().map(BusinessDay::getBreakEndTime).toList();
    List<LocalTime> lastOrders = businessDays.stream().map(BusinessDay::getLastOrderTime).toList();
    List<String> stringDayOffList = new ArrayList<>();
    businessDays.stream().map(BusinessDay::getIsDayOff).forEach(isDayOff -> {
      String[] dayOfWeeks = {"월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일"};
      for (String dayOfWeek : dayOfWeeks) {
        if (isDayOff){
          stringDayOffList.add(dayOfWeek);
        }
        }
      restaurantUpdateDto.setDayOfWeeks(Arrays.stream(dayOfWeeks).toList());
      });

    restaurantUpdateDto.setOpenTimes(openTimes);
    restaurantUpdateDto.setCloseTimes(closeTimes);
    restaurantUpdateDto.setBreakStartTimes(breakStartTimes);
    restaurantUpdateDto.setBreakEndTimes(breakEndTimes);
    restaurantUpdateDto.setLastOrders(lastOrders);
    restaurantUpdateDto.setIsDayOffList(stringDayOffList);


     // Set Food DTO

    List<Food> foods = restaurant.getFoods();
    restaurantUpdateDto.setFoodUpdateDtoList(foods.stream().map(food -> new FoodUpdateDto(food.getName(), food.getPrice(), food.getImage())).toList());

    model.addAttribute("restaurantUpdateDto", restaurantUpdateDto);
    return "restaurant/admin-updateForm";
  }

  @PostMapping("/admin-restaurants/{id}/update")
  public String updateRestaurant(@ModelAttribute("restaurantUpdateDto") RestaurantUpdateDto restaurantUpdateDto , RedirectAttributes redirectAttributes) {

    List<MultipartFile> restaurantImages = restaurantUpdateDto.getRestaurantImages();
    List<MultipartFile> foodImages = new ArrayList<>();
    List<FoodUpdateDto> foodUpdateDtoList = restaurantUpdateDto.getFoodUpdateDtoList();
    for (FoodUpdateDto foodUpdateDto : foodUpdateDtoList) {
      foodImages.add(foodUpdateDto.getFoodImage());
    }

    /**
     * 식당 스토리지 저장 + DB에 스토리지 Url 저장
     */
    List<FileDTO> restaurantImagesFileDto = fileService.uploadFiles(restaurantImages, restaurantDir);
    List<String> restaurantImagesUrl = restaurantImagesFileDto.stream().map(FileDTO::getUploadFileUrl).toList();
    restaurantUpdateDto.setRestaurantImagesUrl(restaurantImagesUrl);

    /**
     * 음식 스토리지 저장 + DB에 스토리지 Url 저장
     */
    List<FileDTO> foodImagesFileDto = fileService.uploadFiles(foodImages, foodDir);
    List<String> foodImagesUrl = foodImagesFileDto.stream().map(FileDTO::getUploadFileUrl).toList();
    for (String foodImageUrl : foodImagesUrl) {
      foodUpdateDtoList.forEach(foodUpdateDto -> {
        foodUpdateDto.setFoodImageUrl(foodImageUrl);
      });
    };

    restaurantService.updateRestaurant(restaurantUpdateDto);
    redirectAttributes.addAttribute("updateId" , restaurantUpdateDto.getId());
    return "redirect:/admin-restaurants/{updateId}";

  }



  private static String convertReservationTimeGapToString(Integer reservationTimeGap) {
    String convertReservationTimeGapToString = switch (reservationTimeGap) {
        case 30 -> "HALF";
        case 60 -> "ONE";
        case 120 -> "TWO";
        default -> "";
    };
      return convertReservationTimeGapToString;
  }




  @GetMapping("admin-restaurants/{id}")
  public String getAdminRestaurant(@PathVariable("id") Long id, Model model) {
    model.addAttribute("restaurant", restaurantService.findOne(id));
    return "restaurant/admin-restaurant";
  }

  @GetMapping("admin-restaurants")
  public String getAdminRestaurants(@ModelAttribute("restaurantAdminSearchCond") RestaurantAdminSearchCond restaurantAdminSearchCond , Model model) {
    model.addAttribute("restaurants", restaurantService.findRestaurantsAdmin(restaurantAdminSearchCond));
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
