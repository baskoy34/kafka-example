package com.volk.service;

import com.volk.model.CustomMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.core.KafkaFailureCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageSenderService {

    private final KafkaTemplate<String, CustomMessage> kafkaTemplate;
    private final TaskExecutor taskExecutor;


    public void send(CustomMessage customMessage) {
        ListenableFuture<SendResult<String, CustomMessage>> future = kafkaTemplate.send("custom-messages", customMessage);
        future.addCallback(result -> {
            log.info("succes");
        }, (KafkaFailureCallback<Integer, String>) ex -> {
            ProducerRecord<Integer, String> failed = ex.getFailedProducerRecord();
            log.error("Error at sending message ex:{}", failed.value());
        });
    }

    private void sendMessages(Date startTime, long messageCount) {
        log.info("Producer started...");
         for (int i = 0; i < messageCount; i++) {
            String value = RandomStringUtils.random(1000, true, true);
            CustomMessage message = CustomMessage.builder().message(value).date(startTime).build();
            send(message);
        }
        log.info("Producer finished.");

    }

    public void sendBulkMessages(int messageCount) {
        int threads = Runtime.getRuntime().availableProcessors();
        final long messagePerThread = messageCount / threads;
        Date now = new Date();
        for (int i = 0; i < threads; i++) {
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    sendMessages(now, messagePerThread);
                }
            });
        }
    }
}
