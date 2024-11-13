package please_do_it.yumi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class RestaurantController {
  @RequestMapping
  public String sddfj(){
    return "index";
  }

  @RequestMapping("/restaurants")
  public String sdfj(){
    return "restaurant/restaurants";
  }


  @RequestMapping("/restaurants/1")
  public String sdf222j(){
    return "restaurant/restaurant";
  }


  @RequestMapping("/abc")
  public String sfdf(){
    return "kyungmin/test-restaurants";
  }

  @RequestMapping("/aaaa")
  public String sfdfddddd(){
    return "fragment/admin-layout";
  }


  @RequestMapping("/bbbb")
  public String dd(){
    return "kyungmin/dddd";
  }








}
