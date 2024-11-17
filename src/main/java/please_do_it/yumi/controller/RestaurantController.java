package please_do_it.yumi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class RestaurantController {


  @GetMapping("/restaurants")
  public String sdfj(){
    return "restaurant/restaurants";
  }

  @GetMapping("/restaurants/1")
  public String sdfj22(){
    return "restaurant/restaurant";
  }

  @GetMapping("aa")
  public String sdj(){
    return "restaurant/admin-restaurants";
  }


  @GetMapping("xx")
  public String asdsdasdj(){
    return "restaurant/saveForm";
  }

  @GetMapping("rr")
  public String fdsjfsjkfjd(){
    return "restaurant/review";
  }








}
