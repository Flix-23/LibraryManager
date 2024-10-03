package com.felixon.author_service.services;

import com.felixon.author_service.models.dtos.AuthorRequest;
import com.felixon.author_service.models.dtos.UserEvent;
import com.felixon.author_service.models.entities.Author;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserConsumer {

    public static String userEventName;

    @KafkaListener(topics = "author-event-topic", groupId = "author-group")
    public void userConsume(UserEvent event){
        try {

            System.out.println("Received user event: " + event.username());

            userEventName = event.username();

        } catch (Exception e) {
            log.error("Error processing user event", e);
        }
    }
}
