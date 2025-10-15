package analytics.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConfig {
    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        String host = System.getenv().getOrDefault("REDIS_HOST", "redis");
        log.info("Connecting to Redis at {}", host);
        return new LettuceConnectionFactory(host, 6379);
    }
}
