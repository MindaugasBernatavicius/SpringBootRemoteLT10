package cf.mindaugas.springbootremotelt10.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // = @Controller + @ResponseBody
public class GreetingController2 {
    @GetMapping("/greeting") // GET /greeting --> "Hello"
    public String getGreeting(){
        return "Hello !!!";
    }
}