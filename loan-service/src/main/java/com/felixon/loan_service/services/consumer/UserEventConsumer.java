package com.felixon.loan_service.services.consumer;

import com.felixon.loan_service.models.dtos.UserEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserEventConsumer {

    public static String userEventName;

    @KafkaListener(topics = "user-topic", groupId = "loan-group")
    public void userEventConsume(UserEvent event){
        try{
            System.out.println("Received user event: " + event.username());

            userEventName = event.username();

        }catch (Exception e){
            log.error("Error processing user event", e);
        }
    }
}
