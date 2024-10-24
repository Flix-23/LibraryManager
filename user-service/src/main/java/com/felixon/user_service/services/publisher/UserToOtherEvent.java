package com.felixon.user_service.services.publisher;

import com.felixon.user_service.models.dtos.UserEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserToOtherEvent {
    @Autowired
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    public void publishUser(UserEvent event){
        this.kafkaTemplate.send("user-topic", event);
    }
}
