package com.felixon.loan_service.services;

import com.felixon.loan_service.DataProvider;
import com.felixon.loan_service.models.dtos.BookLoanRequest;
import com.felixon.loan_service.models.dtos.BookLoanResponse;
import com.felixon.loan_service.models.entities.BookLoan;
import com.felixon.loan_service.repositories.BookLoanRepository;
import com.felixon.loan_service.services.consumer.BookEventConsumer;
import com.felixon.loan_service.services.consumer.UserEventConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    private BookLoanRepository loanRepository;

    @InjectMocks
    private BookLoanServiceImpl loanService;

    private BookLoanRequest loanRequest;

    @BeforeEach
    void setup(){
        loanRequest = new BookLoanRequest();
        loanRequest.setReturned(false);
        loanRequest.setLoanDate(LocalDate.now());
    }

    @DisplayName("Test to find all loan")
    @Test
    void testFindAllLoan(){
        when(loanRepository.findAll()).thenReturn(DataProvider.loanListMock());
        List<BookLoanResponse> bookLoans = loanService.getAllLoan();

        assertNotNull(bookLoans);
        assertFalse(bookLoans.isEmpty());

        assertEquals("La cueva de los indios", bookLoans.get(0).getBookTitle());
        assertEquals("Felixon", bookLoans.get(0).getUsername());

        verify(this.loanRepository).findAll();
    }

    @DisplayName("Test to find loan by id")
    @Test
    void testFindById_whenLoanExist(){
        long id = 1L;

        when(loanRepository.findById(id)).thenReturn(DataProvider.bookLoanMock());
        BookLoanResponse bookLoanResponse = loanService.findById(id);

        assertNotNull(bookLoanResponse);
        assertEquals(id, bookLoanResponse.getId());
        verify(this.loanRepository).findById(anyLong());
    }

    @DisplayName("Test not found loan by id")
    @Test
    void testFindById_whenLoanDoesNotExist(){
        long id = 1L;

        when(loanRepository.findById(id)).thenReturn(Optional.empty());
        BookLoanResponse bookLoanResponse = loanService.findById(id);

        assertNull(bookLoanResponse);

        verify(this.loanRepository).findById(anyLong());
    }

    @DisplayName("Test to save loan")
    @Test
    void testSave(){

        loanRequest = DataProvider.newBookLoanMock();

        BookEventConsumer.bookEventTitle = "La cueva de los indios";
        UserEventConsumer.userEventName = "Felixon";

        BookLoan bookLoan = BookLoan.builder()
                .bookTitle(BookEventConsumer.bookEventTitle)
                .username(UserEventConsumer.userEventName)
                .loanDate(loanRequest.getLoanDate())
                .returned(loanRequest.isReturned())
                .build();

        when(loanRepository.save(any(BookLoan.class))).thenReturn(bookLoan);

        loanService.addLoan(loanRequest);

        assertEquals("La cueva de los indios", bookLoan.getBookTitle());
        assertEquals("Felixon", bookLoan.getUsername());

        verify(loanRepository).save(any(BookLoan.class));
    }

    @DisplayName("Test to update loan")
    @Test
    void testUpdate_whenLoanBookExist(){
        long id = 1L;

        BookLoan existingBook = new BookLoan();

        when(loanRepository.findById(id)).thenReturn(Optional.of(existingBook));
        when(loanRepository.save(any(BookLoan.class))).thenReturn(existingBook);

        BookLoanResponse updatedBook = loanService.updateLoan(id, loanRequest);

        assertNotNull(updatedBook);
        assertEquals(loanRequest.isReturned(), updatedBook.isReturned());

        verify(loanRepository).findById(id);
        verify(loanRepository).save(any(BookLoan.class));

    }

    @DisplayName("Test not found loan to update")
    @Test
    void testUpdate_whenLoanBookDoesNotExist(){
        long id = 1L;

        when(loanRepository.findById(id)).thenReturn(Optional.empty());
        BookLoanResponse updatedBook = loanService.updateLoan(id, loanRequest);

        assertNull(updatedBook);
        verify(loanRepository).findById(id);
        verify(loanRepository,never()).save(any(BookLoan.class));

    }
}
