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
@RequestMapping("/api/review")
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

    @GetMapping("/{title}")
    public ResponseEntity<BookReview> findReviewByTitle(@PathVariable String title){
        Optional<BookReview> optionalBookReview = reviewService.findReviewByTitle(title);
        if (optionalBookReview.isPresent()){
            return ResponseEntity.ok(optionalBookReview.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<?> updateBookReview(@PathVariable String username, @RequestBody BookReviewRequest bookReviewRequest){
        Optional<BookReview> optionalBookReview = reviewService.updateReview(username, bookReviewRequest);

        if (optionalBookReview.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(optionalBookReview.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<BookReview> deleteReview(@PathVariable String username){
        Optional<BookReview> optionalBookReview = reviewService.deleteReview(username);

        if (optionalBookReview.isPresent()){
            return ResponseEntity.ok(optionalBookReview.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }
}
