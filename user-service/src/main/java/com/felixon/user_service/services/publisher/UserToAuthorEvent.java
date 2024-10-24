package com.felixon.user_service.services.publisher;

import com.felixon.user_service.models.dtos.UserEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserToAuthorEvent {

    @Autowired
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    public void publishUserToAuthor(UserEvent event){
        this.kafkaTemplate.send("author-event-topic", event);
    }
}
