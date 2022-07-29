package ejercicio.ampliaiot.rabbitmqproducer;

import lombok.extern.java.Log;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
    public void send(){
        log.info("Preparing to send");
        try {
            Path templateFilePath = ResourceUtils.getFile("classpath:formatoOpenGateTemplate.json").toPath();
            String templateString = Files.readString(templateFilePath);
            Double randomDouble = RandomUtils.nextDouble(0.0, 50);
            String finalMessage = templateString.replace("REPLACEME", randomDouble.toString());
            this.template.convertAndSend(queue.getName(), finalMessage);
            log.info(" [x] Sent '" + finalMessage + "'");
        } catch (FileNotFoundException e) {
            log.info(ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
        } catch (IOException e) {
            log.info(ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
        }
    }
}
