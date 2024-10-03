package com.felixon.author_service.services;

import com.felixon.author_service.models.dtos.AuthorEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuthorEventPublisher {
    @Autowired
    private KafkaTemplate<String, AuthorEvent> kafkaTemplate;

    public void publishAuthorEvent(AuthorEvent authorEvent){

        kafkaTemplate.send("book-events", authorEvent);
        System.out.println("Author event published" + authorEvent);
    }
}
