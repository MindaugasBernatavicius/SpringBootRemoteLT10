package cf.mindaugas.springbootremotelt10.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller // MVC appsai
public class GreetingController {
    @GetMapping("/") // GET / --> "Hello"
    public @ResponseBody
    Map<String, String> getGreeting(){
        // return "Hello !";
        // return new String[]{"Hello", "Labas"};
        Map<String, String> myMap = new HashMap<>();
        myMap.put("Jonas", "Jonaitis");
        myMap.put("Petras", "Petraitis");
        return myMap;
    }
}