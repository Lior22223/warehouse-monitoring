package warehouse.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import warehouse.UdpListener;
import proto.Reading;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UdpListenerTest {

    private Publisher publisher;
    private UdpListener listener;

    @BeforeEach
    void setup() {
        publisher = mock(Publisher.class);
        listener = new UdpListener(publisher, "wh-test", 3344, 3355);
    }

    @Test
    void publishReadingDirectly() {
        Reading reading = Reading.newBuilder()
                .setWarehouseId("wh-test")
                .setSensorId("t1")
                .setType("temperature")
                .setValue(25.5)
                .setTimestamp(System.currentTimeMillis())
                .build();

        publisher.publish(reading);

        ArgumentCaptor<Reading> captor = ArgumentCaptor.forClass(Reading.class);
        verify(publisher, times(1)).publish(captor.capture());
        Reading actual = captor.getValue();

        assertEquals("wh-test", actual.getWarehouseId());
        assertEquals("t1", actual.getSensorId());
        assertEquals("temperature", actual.getType());
        assertEquals(25.5, actual.getValue());
    }
}