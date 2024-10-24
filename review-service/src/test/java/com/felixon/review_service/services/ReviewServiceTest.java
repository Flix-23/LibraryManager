package com.felixon.review_service.services;

import com.felixon.review_service.DataProvider;
import com.felixon.review_service.models.dtos.BookReviewRequest;
import com.felixon.review_service.models.dtos.BookReviewResponse;
import com.felixon.review_service.models.entities.BookReview;
import com.felixon.review_service.repositories.BookReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoOperations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private MongoOperations mongoOperations;

    @Mock
    private BookReviewRepository reviewRepository;

    @InjectMocks
    private BookReviewServiceImpl reviewService;

    private BookReview bookReview;

    private final String username = "Felixon";

    @BeforeEach
    void setUp() {
        bookReview = new BookReview();
        bookReview.setBookTitle("Una vida extraordinaria");
        bookReview.setUsername("Felixon");
        bookReview.setQualification(5);
        bookReview.setDate(LocalDate.now());
    }

    @DisplayName("Test to save review")
    @Test
    void testAddReview() {
        BookReviewRequest bookReviewRequest = new BookReviewRequest();
        when(reviewRepository.save(any(BookReview.class))).thenReturn(bookReview);

        reviewService.addReview(DataProvider.newReviewMock());

        assertEquals("Una vida extraordinaria", bookReview.getBookTitle());
        assertEquals("Felixon", bookReview.getUsername());

        verify(reviewRepository).save(any(BookReview.class));
    }

    @DisplayName("Test to find all review")
    @Test
    void testFindAllReviews() {
        List<BookReview> reviews = Arrays.asList(bookReview);
        when(reviewRepository.findAll()).thenReturn(reviews);

        List<BookReviewResponse> allReviews = reviewService.getAllReview();

        assertEquals(1, allReviews.size());
        assertEquals("Una vida extraordinaria", allReviews.get(0).getBookTitle());
        assertEquals("Felixon", allReviews.get(0).getUsername());

        verify(reviewRepository).findAll();
    }

    @DisplayName("Test to find review by title")
    @Test
    void testFindReviewByTitle() {
        when(reviewRepository.findByBookTitle("Una vida extraordinaria")).thenReturn(Optional.of(bookReview));

        BookReviewResponse foundReview = reviewService.findReviewByTitle(bookReview.getBookTitle());

        assertNotNull(foundReview);
        assertEquals("Una vida extraordinaria", foundReview.getBookTitle());
        assertEquals("Felixon", foundReview.getUsername());

        verify(reviewRepository).findByBookTitle("Una vida extraordinaria");
    }

    @DisplayName("Test not found review by title")
    @Test
    void testFindReviewByTitle_whenReviewDoesNotExist() {
        when(reviewRepository.findByBookTitle("Una vida extraordinaria")).thenReturn(Optional.empty());

        BookReviewResponse foundReview = reviewService.findReviewByTitle(bookReview.getBookTitle());

        assertNull(foundReview);

        verify(reviewRepository).findByBookTitle("Una vida extraordinaria");
    }

    @DisplayName("Test to delete review")
    @Test
    void testDeleteReview() {
        when(reviewRepository.findByUsername(username)).thenReturn(Optional.of(bookReview));
        doNothing().when(reviewRepository).delete(any(BookReview.class));

        BookReviewResponse deletedReview = reviewService.deleteReview(username);

        assertNotNull(deletedReview);
        assertEquals("Una vida extraordinaria", deletedReview.getBookTitle());
        assertEquals("Felixon", deletedReview.getUsername());

        verify(reviewRepository).findByUsername("Felixon");
        verify(reviewRepository).delete(bookReview);
    }

    @DisplayName("Test not found review by username")
    @Test
    void testDeleteReview_whenUserDoesNotExist(){
        when(reviewRepository.findByUsername(username)).thenReturn(Optional.empty());
        BookReviewResponse deletedUser = reviewService.deleteReview(username);

        assertNull(deletedUser);

        verify(reviewRepository).findByUsername(username);
        verify(reviewRepository,never()).delete(any(BookReview.class));
    }


    @DisplayName("Test to update review")
    @Test
    void testUpdateReview() {
        when(reviewRepository.findByUsername(username)).thenReturn(Optional.of(bookReview));

        BookReviewResponse updatedReview = reviewService.updateReview(username, DataProvider.newReviewMock());

        assertNotNull(updatedReview);
        assertEquals("Una vida extraordinaria", updatedReview.getBookTitle());
        assertEquals("Felixon", updatedReview.getUsername());

        verify(reviewRepository).findByUsername(username);
        verify(reviewRepository).save(bookReview);
    }

    @DisplayName("Test not found review to update")
    @Test
    void testUpdateReview_whenReviewDoesNotExist() {
        when(reviewRepository.findByUsername(username)).thenReturn(Optional.empty());

        BookReviewResponse updatedReview = reviewService.updateReview(username, DataProvider.newReviewMock());

        assertNull(updatedReview);

        verify(reviewRepository).findByUsername(bookReview.getUsername());
        verify(reviewRepository,never()).save(bookReview);
    }


}
