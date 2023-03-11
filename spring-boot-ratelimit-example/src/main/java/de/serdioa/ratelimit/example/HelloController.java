package de.serdioa.ratelimit.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {

    @GetMapping("/hello")
    public Message hello(@RequestParam(name = "name", defaultValue = "world") String name) {
        return new Message("Hello " + name);
    }
}
