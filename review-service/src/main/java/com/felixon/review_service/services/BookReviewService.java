package com.felixon.review_service.services;

import com.felixon.review_service.models.dtos.BookReviewRequest;
import com.felixon.review_service.models.dtos.BookReviewResponse;

import java.util.List;

public interface BookReviewService {
    BookReviewResponse addReview(BookReviewRequest reviewRequest);
    List<BookReviewResponse> getAllReview();
    BookReviewResponse findReviewByTitle(String title);
    BookReviewResponse deleteReview(String username);
    BookReviewResponse updateReview(String username, BookReviewRequest bookReviewRequest);

}
