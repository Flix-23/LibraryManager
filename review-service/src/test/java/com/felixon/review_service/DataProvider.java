package com.felixon.review_service;

import com.felixon.review_service.models.dtos.BookReviewRequest;
import com.felixon.review_service.models.dtos.BookReviewResponse;
import com.felixon.review_service.models.entities.BookReview;
import org.springframework.beans.factory.ObjectProvider;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DataProvider {

    public static List<BookReview> reviewListMock(){
        return List.of(new BookReview(1L, "Una vida extraordinaria", "Felixon", 5, "Muy bueno, me ayudo bastante", LocalDate.now()));
    }

    public static List<BookReviewResponse> reviewResponseListMock(){
        return List.of(new BookReviewResponse(1L, "Una vida extraordinaria", "Felixon", 5, "Muy bueno, me ayudo bastante", LocalDate.now()));
    }

    public static Optional<BookReview> reviewMock(){
        return Optional.of(new BookReview(1L, "Una vida extraordinaria", "Felixon", 5, "Muy bueno", LocalDate.now()));
    }

    public static BookReviewRequest newReviewMock(){
        return new BookReviewRequest(1L,"Una vida extraordinaria", "felixon", 5, "Muy bueno", LocalDate.now());
    }

}
