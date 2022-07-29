package ejercicio.ampliaiot.rabbitmqproducer;

import lombok.extern.java.Log;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Log
public class Sender {
    @Autowired
    private RabbitTemplate template;

    @Autowired
    private Queue queue;

    @Scheduled(fixedDelay = 2000, initialDelay = 500)
    public void send() throws IOException {
            Path templateFilePath = ResourceUtils.getFile("classpath:formatoOpenGateTemplate.json").toPath();
            String templateString = Files.readString(templateFilePath);
            Double randomDouble = RandomUtils.nextDouble(0.0, 50);
            String finalMessage = templateString.replace("REPLACEME",randomDouble.toString());
            this.template.convertAndSend(queue.getName(), finalMessage);
            log.info(" [x] Sent '" + finalMessage + "'");
    }
}
