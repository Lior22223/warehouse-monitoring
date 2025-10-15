package analytics.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AnalyticsController {

    private final StringRedisTemplate redis;

    public AnalyticsController(StringRedisTemplate redis) {
        this.redis = redis;
    }

    @GetMapping("/api/readings")
    public Map<String, String> getAllReadings() {
        return redis.keys("warehouse:*").stream()
                .collect(Collectors.toMap(k -> k, redis.opsForValue()::get));
    }
}
