package com.felixon.review_service.services.consumer;

import com.felixon.review_service.models.dtos.UserEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserEventConsumer {

    public static String userEventName;

    @KafkaListener(topics = "user-topic", groupId = "book-review")
    public void userEventConsume(UserEvent userEvent){
        try {

            System.out.println("Received user event: " + userEvent.username());

            userEventName = userEvent.username();

        } catch (Exception e) {
            log.error("Error processing user event", e);
        }
    }
}
