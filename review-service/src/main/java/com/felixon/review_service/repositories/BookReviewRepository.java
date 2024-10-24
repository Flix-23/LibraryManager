package com.felixon.review_service.repositories;

import com.felixon.review_service.models.entities.BookReview;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookReviewRepository extends MongoRepository<BookReview, Long> {
    Optional<BookReview> findByUsername(String username);
    List<BookReview> findAll();
    Optional<BookReview> findByBookTitle(String title);
}
