package de.serdioa.ratelimit.example;

import de.serdioa.ratelimit.RateLimitHttpHeaders;
import de.serdioa.ratelimit.RateLimitProbe;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class HelloController {

    @Autowired
    private RateLimitService rateLimitService;

    @Autowired
    private HttpSession httpSession;


    @GetMapping("/hello")
    public Message hello(HttpServletResponse response, @RequestParam(name = "name", defaultValue = "world") String name) {
        final RateLimitProbe probe = this.rateLimitService.tryConsume(this.httpSession, "hello");
        response.setHeader(RateLimitHttpHeaders.LIMIT, RateLimitHttpHeaders.limit(probe));
        response.setHeader(RateLimitHttpHeaders.POLICY, RateLimitHttpHeaders.policy(probe));
        response.setHeader(RateLimitHttpHeaders.REMAINING, RateLimitHttpHeaders.remaining(probe));
        response.setHeader(RateLimitHttpHeaders.RESET, RateLimitHttpHeaders.reset(probe));

        if (!probe.isConsumed()) {
            response.setHeader(HttpHeaders.RETRY_AFTER, RateLimitHttpHeaders.retryAfter(probe));
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS);
        }

        return new Message("Hello " + name);
    }
}
