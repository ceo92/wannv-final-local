package please_do_it.yumi.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import please_do_it.yumi.constant.ContainFoodType;
import please_do_it.yumi.constant.MoodType;
import please_do_it.yumi.constant.ProvideServiceType;
import please_do_it.yumi.constant.RestaurantType;
import please_do_it.yumi.domain.Restaurant;
import please_do_it.yumi.dto.RestaurantSearchCond;
import please_do_it.yumi.service.RestaurantService;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/restaurants")
public class RestaurantController {

  private final RestaurantService restaurantService;

  @ModelAttribute("containFoodTypes")
  public ContainFoodType[] containFoodTypes(){
    return ContainFoodType.values();
  }

  @ModelAttribute("provideServiceTypes")
  public ProvideServiceType[] provideServiceTypes(){
    return ProvideServiceType.values();
  }

  @ModelAttribute("restaurantTypes")
  public RestaurantType[] restaurantTypes(){
    return RestaurantType.values();
  }

  @ModelAttribute("moodTypes")
  public MoodType[] moodTypes(){
    return MoodType.values();
  }


  @GetMapping
  public String getRestaurants(@ModelAttribute("restaurantSearchCond") RestaurantSearchCond restaurantSearchCond, Model model){
    List<Restaurant> restaurants = restaurantService.findRestaurants(restaurantSearchCond);
    model.addAttribute("restaurants", restaurants);
    return "restaurant/restaurants";
  }

  @GetMapping("/{id}")
  public String getRestaurant(@PathVariable Long id, Model model) {
    model.addAttribute("restaurant", restaurantService.findOne(id));
    return "restaurant/restaurant";
  }






}
