package com.felixon.review_service.services.consumer;


import com.felixon.review_service.models.dtos.BookEventDto;
import com.felixon.review_service.models.dtos.BookReviewRequest;
import com.felixon.review_service.services.BookReviewServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookEventConsumer {

    public static String bookEventTitle;

    @KafkaListener(topics = "book-review-topic",
            groupId = "${spring.kafka.consumer.group-id}")
    public void consume(BookEventDto bookEvent){
        try {
            System.out.println("Book event received " + bookEvent);

            bookEventTitle = bookEvent.title();

        } catch (Exception e) {
            log.error("Error processing book event", e);
        }
    }
}
