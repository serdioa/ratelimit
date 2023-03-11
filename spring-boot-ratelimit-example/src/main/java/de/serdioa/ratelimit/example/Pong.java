package de.serdioa.ratelimit.example;

import java.time.ZonedDateTime;

import lombok.Data;


@Data
public class Pong {

    private final String token;
    private final ZonedDateTime timestamp;


    public Pong(String token) {
        this.token = token;
        this.timestamp = ZonedDateTime.now();
    }
}
