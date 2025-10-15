package central.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import proto.Reading;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class CentralMonitoringServiceTest {

    private StringRedisTemplate redis;
    private ValueOperations<String, String> valueOps;
    private CentralMonitoringService service;

    @BeforeEach
    void setup() {
        redis = mock(StringRedisTemplate.class);
        valueOps = mock(ValueOperations.class);
        when(redis.opsForValue()).thenReturn(valueOps);
        service = new CentralMonitoringService(redis);
    }

    @Test
    void temperatureAboveThreshold() {
        Reading reading = Reading.newBuilder()
                .setWarehouseId("wh1")
                .setSensorId("t1")
                .setType("temperature")
                .setValue(40.0)
                .setTimestamp(System.currentTimeMillis())
                .build();

        service.handle(reading.toByteArray());

        verify(valueOps, times(1)).set(eq("warehouse:wh1:temperature:t1"), eq("40.0"));
    }

    @Test
    void humidityBelowThreshold() {
        Reading reading = Reading.newBuilder()
                .setWarehouseId("wh2")
                .setSensorId("h1")
                .setType("humidity")
                .setValue(45.0)
                .setTimestamp(System.currentTimeMillis()).build();

        service.handle(reading.toByteArray());

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        verify(valueOps, times(1)).set(eq("warehouse:wh2:humidity:h1"), eq("45.0"));
    }
}