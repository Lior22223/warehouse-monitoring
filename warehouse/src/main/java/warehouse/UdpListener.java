package warehouse;

import proto.Reading;
import warehouse.service.Publisher;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Arrays;

@Component
public class UdpListener {
    private final Publisher publisher;
    private final String warehouseId;
    private final int tempPort, humPort;

    public UdpListener(Publisher publisher, @Value("${WAREHOUSE_ID:wh1}") String warehouseId, @Value("${UDP_PORT_TEMP:3344}") int tempPort, @Value("${UDP_PORT_HUM:3355}") int humPort) {
        this.publisher = publisher;
        this.warehouseId = warehouseId;
        this.tempPort = tempPort;
        this.humPort = humPort;
    }

    @PostConstruct
    void start() {
        startThread(tempPort, "temperature");
        startThread(humPort, "humidity");
    }

    private void startThread(int port, String type) {
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket(port)) {
                byte[] buf = new byte[1024];
                while (true) {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    String msg = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8).trim();
                    Map<String, String> kv = Arrays.stream(msg.split(";"))
                            .map(String::trim)
                            .filter(s -> s.contains("="))
                            .map(s -> s.split("=", 2))
                            .collect(Collectors.toMap(a -> a[0].trim(), a -> a[1].trim()));
                    Reading reading = Reading.newBuilder()
                            .setWarehouseId(warehouseId)
                            .setSensorId(kv.get("sensor_id"))
                            .setType(type)
                            .setValue(Double.parseDouble(kv.get("value")))
                            .setTimestamp(System.currentTimeMillis())
                            .build();
                    publisher.publish(reading);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "udp-" + type).start();
    }
}
