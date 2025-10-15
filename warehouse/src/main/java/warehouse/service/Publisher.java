package warehouse.service;

import proto.Reading;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class Publisher {
    private final RabbitTemplate template;

    public Publisher(RabbitTemplate template) {
        this.template = template;
    }

    public void publish(Reading reading) {
        byte[] data = reading.toByteArray();
        MessageProperties props = new MessageProperties();
        props.setContentType("application/x-protobuf");
        template.send("readings", "reading." + reading.getType(), new Message(data, props));
    }
}
