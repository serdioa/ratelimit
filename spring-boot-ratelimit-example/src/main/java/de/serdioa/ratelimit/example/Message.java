package de.serdioa.ratelimit.example;

import java.time.ZonedDateTime;

import lombok.Data;


@Data
public class Message {

    private final String text;
    private final ZonedDateTime timestamp;


    public Message(String text) {
        this.text = text;
        this.timestamp = ZonedDateTime.now();
    }
}
