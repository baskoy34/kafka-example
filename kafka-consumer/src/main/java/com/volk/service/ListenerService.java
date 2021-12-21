package com.volk.service;

import com.volk.model.CustomMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ListenerService {

    @KafkaListener(topics = "custom-messages")
    public void listenCustomMessage(CustomMessage customMessage, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        System.out.println(
                "Received Message: " + customMessage
                        + "from partition: " + partition);
    }
}
