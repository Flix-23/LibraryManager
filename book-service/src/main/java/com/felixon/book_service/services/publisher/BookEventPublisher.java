package com.felixon.book_service.services.publisher;

import com.felixon.book_service.models.dtos.BookEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookEventPublisher {

    @Autowired
    private KafkaTemplate<String, BookEvent> kafkaTemplate;


    public void publishBook(BookEvent bookEvent){
        kafkaTemplate.send("book-review-topic", bookEvent);
        kafkaTemplate.send("book-loan-topic", bookEvent);
        System.out.println("Book event published" + bookEvent);
    }


}
