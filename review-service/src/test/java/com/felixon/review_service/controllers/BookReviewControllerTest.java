package com.felixon.review_service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felixon.review_service.DataProvider;
import com.felixon.review_service.models.dtos.BookReviewRequest;
import com.felixon.review_service.models.dtos.BookReviewResponse;
import com.felixon.review_service.models.entities.BookReview;
import com.felixon.review_service.services.BookReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookReviewController.class)
public class BookReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookReviewServiceImpl bookReviewService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookReviewRequest bookReviewRequest;

    private BookReviewResponse bookReview;

    @BeforeEach
    void setup(){
        bookReviewRequest = BookReviewRequest.builder()
                .bookTitle("Una vida extraordinaria")
                .username("Felixon")
                .qualification(5)
                .comment("Muy bueno")
                .date(LocalDate.now())
                .build();

        ModelMapper model = new ModelMapper();
        bookReview = model.map(bookReviewRequest, BookReviewResponse.class);
    }

    @DisplayName("Test book review controller save")
    @Test
    void testSaveBookReview() throws Exception{
        when(bookReviewService.addReview(DataProvider.newReviewMock())).thenReturn(bookReview);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/review/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bookTitle\":\"Una vida extraordinaria\", " +
                                "\"username\":\"Felixon\", \"qualification\":5," +
                                " \"comment\":\"Muy bueno\", " +
                                "\"date\":\"" + LocalDate.now() + "\"}"))
                        .andExpect(status().isCreated());
    }

    @DisplayName("Test find all book review")
    @Test
    void testFindAll() throws Exception{
        when(bookReviewService.getAllReview()).thenReturn(DataProvider.reviewResponseListMock());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/review")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
    }

    @DisplayName("Test find book review by title")
    @Test
    void testFindByTitle_whenBookReviewExist() throws Exception{
        when(bookReviewService.findReviewByTitle(bookReviewRequest.getBookTitle())).thenReturn(bookReview);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/review/Una vida extraordinaria")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                         .andExpect(jsonPath("$.bookTitle").value(bookReview.getBookTitle()));
    }

    @DisplayName("Test not found book review by title")
    @Test
    void testFindByTitle_whenBookReviewDoesNotExist() throws Exception{
        when(bookReviewService.findReviewByTitle(bookReview.getBookTitle())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/review/Una vida extraordinaria")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
    }

    @DisplayName("Test update book")
    @Test
    void testUpdateBookReview_whenAuthorExist() throws Exception{
        when(bookReviewService.updateReview("felixon", DataProvider.newReviewMock())).thenReturn(bookReview);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/review/update/felixon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\", \"bookTitle\":\"Una vida extraordinaria\", " +
                                "\"username\":\"felixon\", \"qualification\":5," +
                                " \"comment\":\"Muy bueno\", " +
                                "\"date\":\"" + LocalDate.now() + "\"}"))
                        .andExpect(status().isCreated());


    }

    @DisplayName("Test delete book")
    @Test
    void testDeleteAuthor_whenAuthorExist() throws Exception{
        when(bookReviewService.deleteReview(bookReviewRequest.getUsername())).thenReturn(bookReview);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/review/delete/Felixon")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(bookReview.getUsername()));

    }





}
