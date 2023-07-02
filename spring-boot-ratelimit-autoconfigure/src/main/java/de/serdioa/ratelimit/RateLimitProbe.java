package de.serdioa.ratelimit;

import io.github.bucket4j.ConsumptionProbe;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class RateLimitProbe {

    private final String key;
    private final ConsumptionProbe probe;
    private final BucketConfiguration configuration;


    public boolean isConsumed() {
        return this.probe.isConsumed();
    }
}
