package please_do_it.yumi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class PleaseIBeggingYou {
  @RequestMapping
  public String sddfj(){
    return "index";
  }

  @RequestMapping("/restaurants")
  public String sdfj(){
    return "restaurant/restaurants";
  }


  @RequestMapping("/restaurant")
  public String sdf222j(){
    return "restaurant/restaurant";
  }


  @RequestMapping("/abc")
  public String sfdf(){
    return "restaurant/";
  }




}
