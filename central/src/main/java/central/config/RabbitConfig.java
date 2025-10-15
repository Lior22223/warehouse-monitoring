package central.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    private static final String EXCHANGE = "readings";
    private static final String QUEUE = "central.readings";

    @Bean
    public TopicExchange readingsExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue readingsQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Binding temperatureBinding(Queue readingsQueue, TopicExchange readingsExchange) {
        return BindingBuilder.bind(readingsQueue).to(readingsExchange).with("reading.temperature");
    }

    @Bean
    public Binding humidityBinding(Queue readingsQueue, TopicExchange readingsExchange) {
        return BindingBuilder.bind(readingsQueue).to(readingsExchange).with("reading.humidity");
    }
}
