package central.service;

import proto.Reading;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CentralMonitoringService {
    private static final Logger log = LoggerFactory.getLogger(CentralMonitoringService.class);
    private final StringRedisTemplate redis;
    private final double tempThreshold, humThreshold;

    public CentralMonitoringService(StringRedisTemplate redis) {
        this.redis = redis;
        this.tempThreshold = Double.parseDouble(System.getenv().getOrDefault("TEMP_THRESHOLD", "35"));
        this.humThreshold = Double.parseDouble(System.getenv().getOrDefault("HUM_THRESHOLD", "50"));
    }

    @RabbitListener(queues = "central.readings")
    public void handle(byte[] body) {
        try {
            Reading r = Reading.parseFrom(body);

            // TODO: Remove
            log.debug("[DEBUG] Received reading: {}", r);

            String key = "warehouse:" + r.getWarehouseId() + ":" + r.getType() + ":" + r.getSensorId();
            redis.opsForValue().set(key, String.valueOf(r.getValue()));

            // TODO: Remove
            log.debug("[DEBUG] Saved to Redis key={} value={}", key, r.getValue());

            switch (r.getType()) {
                case "temperature" -> checkTemp(r);
                case "humidity" -> checkHum(r);
                default -> log.info("Unknown type {}", r.getType());
            }
        } catch (Exception e) {
            log.error("decode", e);
        }
    }

    private void checkTemp(Reading r) {
        if (r.getValue() > tempThreshold) {
            log.warn("(!) TEMP ALERT {}>{} @ {}", r.getValue(), tempThreshold, r.getWarehouseId());
        } else {
            log.info("Temp OK {} @ {}", r.getValue(), r.getWarehouseId());
        }
    }

    private void checkHum(Reading r) {
        if (r.getValue() > humThreshold) {
            log.warn("(!) HUM ALERT {}>{} @ {}", r.getValue(), humThreshold, r.getWarehouseId());
        } else {
            log.info("Hum OK {} @ {}", r.getValue(), r.getWarehouseId());
        }
    }
}
