package com.felixon.loan_service.services.consumer;


import com.felixon.loan_service.models.dtos.BookEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookEventConsumer {

    public static String bookEventTitle;

    @KafkaListener(topics = "book-loan-topic",
            groupId = "loan-group")
    public void consume(BookEvent bookEvent){
        try {
            System.out.println("Book event received " + bookEvent.title());

            bookEventTitle = bookEvent.title();

        } catch (Exception e) {
            log.error("Error processing user event", e);
        }
    }
}
