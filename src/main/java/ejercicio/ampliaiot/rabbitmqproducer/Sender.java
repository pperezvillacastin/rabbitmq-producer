package ejercicio.ampliaiot.rabbitmqproducer;

import lombok.extern.java.Log;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.FileCopyUtils;
import java.io.*;
import java.nio.charset.StandardCharsets;

@Log
public class Sender {
    @Value("${rabbitmq.messagesperbatch}")
    private String rabbitmqMessagesPerBatchString;

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private Queue queue;

    @Value("classpath:formatoOpenGateTemplate.json")
    private Resource templateResource;

    @Scheduled(fixedDelayString = "${queue.write.rate}", initialDelay = 3000)
    public void send(){
        log.info("Preparing to send");
        try {
            String templateString = FileCopyUtils.copyToString(new InputStreamReader(templateResource.getInputStream(), StandardCharsets.UTF_8));
            for (int i=0; i<=Integer.valueOf(rabbitmqMessagesPerBatchString)-1; i++) {
                //Generar on double aleatorio pero rendondearlo a un decimal
                Double randomDouble = Math.round(RandomUtils.nextDouble(0.0, 50)*10.0)/10.0;
                String finalMessage = templateString.replace("REPLACEME", randomDouble.toString());
                this.template.convertAndSend(queue.getName(), finalMessage);
                log.info(" [" + i + "] Sent '" + finalMessage + "'");
            }
        } catch (FileNotFoundException e) {
            log.info(ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
        } catch (IOException e) {
            log.info(ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
        }
    }
}
