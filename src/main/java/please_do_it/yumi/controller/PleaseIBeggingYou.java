package please_do_it.yumi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class PleaseIBeggingYou {

  @RequestMapping
  public String fsduisdfiu(){
    log.info("sfdjsdfju");
    return "fragment/layout";
  }

}
