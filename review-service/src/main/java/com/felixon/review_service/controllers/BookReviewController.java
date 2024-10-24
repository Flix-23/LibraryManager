package com.felixon.review_service.controllers;

import com.felixon.review_service.models.dtos.BookReviewRequest;
import com.felixon.review_service.models.dtos.BookReviewResponse;
import com.felixon.review_service.models.entities.BookReview;
import com.felixon.review_service.services.BookReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/review")
public class BookReviewController {

    @Autowired
    private BookReviewService reviewService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public void addBookReview(@RequestBody BookReviewRequest reviewRequest){
        this.reviewService.addReview(reviewRequest);
    }

    @GetMapping
    public List<BookReviewResponse> getAllReview(){
        return reviewService.getAllReview();
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<BookReviewResponse> findReviewByTitle(@PathVariable String title){
        BookReviewResponse bookReviewResponse = reviewService.findReviewByTitle(title);
        if (bookReviewResponse != null){
            return ResponseEntity.ok(bookReviewResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<BookReviewResponse> updateBookReview(@PathVariable String username, @RequestBody BookReviewRequest bookReviewRequest){
        BookReviewResponse bookReviewResponse = reviewService.updateReview(username, bookReviewRequest);

        if (bookReviewResponse != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(bookReviewResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<BookReviewResponse> deleteReview(@PathVariable String username){
        BookReviewResponse bookReviewResponse = reviewService.deleteReview(username);

        if (bookReviewResponse != null){
            return ResponseEntity.ok(bookReviewResponse);
        }
        return ResponseEntity.notFound().build();
    }
}
