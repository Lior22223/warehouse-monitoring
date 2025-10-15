package analytics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@EnableScheduling
@Component
public class Poller {
    private static final Logger log = LoggerFactory.getLogger(Poller.class);
    private final StringRedisTemplate redis;

    public Poller(StringRedisTemplate redis) {
        this.redis = redis;
    }

    @Scheduled(fixedDelay = 10000)
    public void show() {
        Set<String> keys = redis.keys("warehouse:*");
        if (keys == null || keys.isEmpty()) {
            log.info("No data yet");
            return;
        }
        log.info("=== Snapshot ===");
        keys.forEach(k -> log.info("{} = {}", k, redis.opsForValue().get(k)));
    }
}
