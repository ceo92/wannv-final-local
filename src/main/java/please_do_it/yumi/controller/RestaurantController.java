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

  @GetMapping("dd")
  public String ssddj(){
    return "restaurant/admin-restaurant";
  }


  @GetMapping("xx")
  public String asdsdasdj(){
    return "restaurant/saveForm";
  }

  @GetMapping("rr")
  public String fdsjfsjkfjd(){
    return "restaurant/admin-saveForm";
  }

  @GetMapping("rrr")
  public String fdsjfsjddkfjd(){
    return "restaurant/admin-updateForm";
  }




  @GetMapping("vv")
  public String sdfjusd(){
    return "restaurant/saveForm";
  }








}
