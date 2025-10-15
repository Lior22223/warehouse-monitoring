package central.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {
    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        String redisHost = System.getenv().getOrDefault("REDIS_HOST", "redis");
        log.info("Connecting to Redis at {}", redisHost);
        return new LettuceConnectionFactory(redisHost, 6379);
    }

    @Bean
    public StringRedisTemplate redisTemplate(RedisConnectionFactory factory) {
        return new StringRedisTemplate(factory);
    }
}
