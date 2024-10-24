package com.felixon.loan_service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felixon.loan_service.DataProvider;
import com.felixon.loan_service.models.dtos.BookLoanRequest;
import com.felixon.loan_service.models.dtos.BookLoanResponse;
import com.felixon.loan_service.models.entities.BookLoan;
import com.felixon.loan_service.services.BookLoanServiceImpl;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookLoanController.class)
public class LoanControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookLoanServiceImpl bookLoanService;


    private BookLoanRequest bookLoanRequest;
    private BookLoanResponse bookLoan;

    @BeforeEach
    void setup(){
        bookLoanRequest = BookLoanRequest.builder()
                .username("Perla")
                .bookTitle("Una vida extraordinaria")
                .loanDate(LocalDate.now())
                .returned(false)
                .build();

        ModelMapper model = new ModelMapper();
        bookLoan = model.map(bookLoanRequest, BookLoanResponse.class);

    }

    @DisplayName("Test book loan save")
    @Test
    void testSaveBookLoan() throws Exception{
        when(bookLoanService.addLoan(DataProvider.newBookLoanMock())).thenReturn(bookLoan);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/loan/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"Perla\", " +
                                "\"bookTitle\":\"Una vida extraordinaria\", " +
                                "\"loanDate\":\"" + LocalDate.now() + "\", " +
                                "\"returned\":false }"))
                        .andExpect(status().isCreated());
    }

    @DisplayName("Test find all book loan")
    @Test
    void testFindAll() throws Exception{
        when(bookLoanService.getAllLoan()).thenReturn(DataProvider.loanResponsesMock());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/loan")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
    }

    @DisplayName("Test find book loan by id")
    @Test
    void testFindById_whenBookExist() throws Exception{
        when(bookLoanService.findById(1l)).thenReturn(bookLoan);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/loan/" + bookLoan.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.username").value(bookLoan.getUsername()));
    }

    @DisplayName("Test not found book loan by id")
    @Test
    void testFindById_whenBookDoesNotExist() throws Exception{
        when(bookLoanService.findById(1l)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/loan/" + bookLoan.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Test update book loan")
    @Test
    void testUpdateAuthor_whenAuthorExist() throws Exception{
        when(bookLoanService.updateLoan(1L, DataProvider.newBookLoanMock())).thenReturn(bookLoan);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/loan/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"Perla\", " +
                                "\"bookTitle\":\"Una vida extraordinaria\", " +
                                "\"loanDate\":\"" + LocalDate.now() + "\", " +
                                "\"returned\":true }"))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.bookTitle").value(bookLoan.getBookTitle()));

    }

    @DisplayName("Test not found book loan to update")
    @Test
    void testUpdateAuthor_whenAuthorDoesNotExist() throws Exception{
        when(bookLoanService.updateLoan(1L, DataProvider.newBookLoanMock())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/loan/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"Perla\", " +
                                "\"bookTitle\":\"Una vida extraordinaria\", " +
                                "\"loanDate\":\"" + LocalDate.now() + "\", " +
                                "\"returned\":true }"))
                        .andExpect(status().isNotFound());

    }
}
