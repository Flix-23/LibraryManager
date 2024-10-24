package com.felixon.review_service.services;

import com.felixon.review_service.models.dtos.BookReviewRequest;
import com.felixon.review_service.models.dtos.BookReviewResponse;
import com.felixon.review_service.models.entities.BookReview;
import com.felixon.review_service.models.entities.DBSequence;
import com.felixon.review_service.repositories.BookReviewRepository;
import com.felixon.review_service.services.consumer.BookEventConsumer;
import com.felixon.review_service.services.consumer.UserEventConsumer;
import org.modelmapper.ModelMapper;
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

    private ModelMapper model = new ModelMapper();

    @Override
    @Transactional
    public BookReviewResponse addReview(BookReviewRequest reviewRequest) {
            var review = BookReview.builder()
                    .id(generateSequence(BookReview.SEQUENCE_NAME))
                    .bookTitle(BookEventConsumer.bookEventTitle)
                    .username(UserEventConsumer.userEventName)
                    .qualification(reviewRequest.getQualification())
                    .comment(reviewRequest.getComment())
                    .build();

            this.reviewRepository.save(review);

            return model.map(review, BookReviewResponse.class);
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
        var reviews = reviewRepository.findAll();
                
        return  reviews.stream().map(this::mapToBookReviewResponse).toList();
    }

    private BookReviewResponse mapToBookReviewResponse(BookReview bookReview) {
        return model.map(bookReview, BookReviewResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public BookReviewResponse findReviewByTitle(String title) {
        Optional<BookReview> bookReview = this.reviewRepository.findByBookTitle(title);
        return model.map(bookReview, BookReviewResponse.class);
    }

    @Override
    @Transactional
    public BookReviewResponse deleteReview(String username) {
        Optional<BookReview> optionalBookReview = reviewRepository.findByUsername(username);
        optionalBookReview.ifPresent(reviewRepository::delete);

        return model.map(optionalBookReview, BookReviewResponse.class);
    }

    @Override
    @Transactional
    public BookReviewResponse updateReview(String username, BookReviewRequest bookReviewRequest) {
        Optional<BookReview> optionalBookReview = reviewRepository.findByUsername(username);

        optionalBookReview.ifPresent(bookReviewDB -> {
            bookReviewDB.setQualification(bookReviewRequest.getQualification());
            bookReviewDB.setComment(bookReviewRequest.getComment());

            this.reviewRepository.save(bookReviewDB);
        });
        return model.map(optionalBookReview, BookReviewResponse.class);
    }
}
