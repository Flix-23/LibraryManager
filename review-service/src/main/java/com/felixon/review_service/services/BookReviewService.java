package com.felixon.review_service.services;

import com.felixon.review_service.models.dtos.BookReviewRequest;
import com.felixon.review_service.models.dtos.BookReviewResponse;
import com.felixon.review_service.models.entities.BookReview;

import java.util.List;
import java.util.Optional;

public interface BookReviewService {
    void addReview(BookReviewRequest reviewRequest);
    List<BookReviewResponse> getAllReview();
    Optional<BookReview> findReviewByTitle(String title);
    Optional<BookReview> deleteReview(String username);
    Optional<BookReview> updateReview(String username, BookReviewRequest bookReviewRequest);

}
