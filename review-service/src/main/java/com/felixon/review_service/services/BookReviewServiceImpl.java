package com.felixon.review_service.services;

import com.felixon.review_service.models.dtos.BookReviewRequest;
import com.felixon.review_service.models.dtos.BookReviewResponse;
import com.felixon.review_service.models.entities.BookReview;
import com.felixon.review_service.models.entities.DBSequence;
import com.felixon.review_service.repositories.BookReviewRepository;
import com.felixon.review_service.services.consumer.BookEventConsumer;
import com.felixon.review_service.services.consumer.UserEventConsumer;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class BookReviewServiceImpl implements BookReviewService{

    @Autowired
    private BookReviewRepository reviewRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    @Transactional
    public void addReview(BookReviewRequest reviewRequest) {
            var review = BookReview.builder()
                    .id(generateSequence(BookReview.SEQUENCE_NAME))
                    .bookTitle(BookEventConsumer.bookEventTitle)
                    .username(UserEventConsumer.userEventName)
                    .qualification(reviewRequest.getQualification())
                    .comment(reviewRequest.getComment())
                    .build();

            this.reviewRepository.save(review);
    }

    public long generateSequence(String seqName) {
        DBSequence counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
                new Update().inc("seq",1), options().returnNew(true).upsert(true),
                DBSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }


    @Override
    @Transactional(readOnly = true)
    public List<BookReviewResponse> getAllReview() {
        var reviews = reviewRepository.findAllReview();
                
        return  reviews.stream().map(this::mapToBookReviewResponse).toList();
    }

    private BookReviewResponse mapToBookReviewResponse(BookReview bookReview) {

        return BookReviewResponse.builder()
                .id(bookReview.getId())
                .bookTitle(bookReview.getBookTitle())
                .username(bookReview.getUsername())
                .qualification(bookReview.getQualification())
                .comment(bookReview.getComment())
                .date(bookReview.getDate())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookReview> findReviewByTitle(String title) {
        return this.reviewRepository.findByBookTitle(title);
    }

    @Override
    @Transactional
    public Optional<BookReview> deleteReview(String username) {
        Optional<BookReview> optionalBookReview = reviewRepository.findByUsername(username);
        optionalBookReview.ifPresent(reviewRepository::delete);

        return optionalBookReview;
    }

    @Override
    @Transactional
    public Optional<BookReview> updateReview(String username, BookReviewRequest bookReviewRequest) {
        Optional<BookReview> optionalBookReview = reviewRepository.findByUsername(username);

        optionalBookReview.ifPresent(bookReviewDB -> {
            bookReviewDB.setQualification(bookReviewRequest.getQualification());
            bookReviewDB.setComment(bookReviewRequest.getComment());

            this.reviewRepository.save(bookReviewDB);
        });
        return optionalBookReview;
    }
}
