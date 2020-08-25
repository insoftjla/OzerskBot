package sd.insoft.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class mainController {

    @GetMapping("/")
    public String mainPage(){
        return "redirect:https://t.me/ozersk39bot";
    }

}
