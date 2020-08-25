package sd.insoft.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WakeMyAppController {

    @GetMapping("/wakemyapp")
    public String wakeMyApp(){
        return "wakeUp";
    }
}
