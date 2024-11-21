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
import please_do_it.yumi.domain.Restaurant;
import please_do_it.yumi.dto.RestaurantSearchCond;
import please_do_it.yumi.service.RestaurantService;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RestaurantController {

  private final RestaurantService restaurantService;


 /* @GetMapping("/restaurants")
  public String getRestaurants(@ModelAttribute RestaurantSearchCond restaurantSearchCond, Model model){
    List<Restaurant> restaurants = restaurantService.findRestaurants(restaurantSearchCond);
    model.addAttribute("restaurants", restaurants);
    return "restaurant/restaurants";
  }

  @GetMapping("/restaurants/{id}")
  public String getRestaurant(@PathVariable Long id, Model model) {
    model.addAttribute("restaurant", restaurantService.findOne(id));
    return "restaurant/restaurant";
  }*/

  @GetMapping("/restaurants")
  public String sdkjds(){
    return "restaurant/restaurants";
  }


  @GetMapping("/restaurants/1")
  public String dfsjdf(){
    return "restaurant/restaurant";
  }



  @GetMapping("/admin/restaurants")
  public String fdskjd(){
    return "restaurant/admin-restaurants";
  }
  @GetMapping("/admin/restaurant")
  public String fdskjddd(){
    return "restaurant/admin-restaurant";
  }

  @GetMapping("/admin/save")
  public String fdskjddddd(){
    return "restaurant/admin-saveForm";
  }

  @GetMapping("/admin/update")
  public String fdskjddddddd(){
    return "restaurant/admin-updateForm";
  }






}
