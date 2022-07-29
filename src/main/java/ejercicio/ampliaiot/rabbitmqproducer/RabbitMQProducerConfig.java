package ejercicio.ampliaiot.rabbitmqproducer;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQProducerConfig {
    @Value("${rabbitmq.producer.queuename}")
    private String queueName;
    @Bean
    public Queue write(){
        return new Queue(queueName);
    }
    @Bean
    public Sender sender() {
        return new Sender();
    }
}
