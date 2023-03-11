package de.serdioa.ratelimit.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PingController {

    @GetMapping("/ping")
    public Pong ping(@RequestParam(name = "token", defaultValue = "pong") String token) {
        return new Pong(token);
    }
}
