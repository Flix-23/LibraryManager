package com.felixon.book_service.services.consumer;

import com.felixon.book_service.models.dtos.AuthorEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthorEventConsumer {

    public static String authorEventName;

    @KafkaListener(topics = "book-events", groupId = "book-group")
    public void consume(AuthorEvent authorEvent){
        try {

            System.out.println("Received author event: " + authorEvent.name());

            authorEventName = authorEvent.name();

        } catch (Exception e) {
            log.error("Error processing book event", e);
        }
    }
}
