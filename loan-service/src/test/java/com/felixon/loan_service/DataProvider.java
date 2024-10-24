package com.felixon.loan_service;

import com.felixon.loan_service.models.dtos.BookLoanRequest;
import com.felixon.loan_service.models.dtos.BookLoanResponse;
import com.felixon.loan_service.models.entities.BookLoan;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DataProvider {

    public static List<BookLoan> loanListMock(){
        return List.of(new BookLoan(1L, "Felixon", "La cueva de los indios", LocalDate.now(),false));
    }

    public static List<BookLoanResponse> loanResponsesMock(){
        return List.of(new BookLoanResponse(1L, "Felixon", "La cueva de los indios", LocalDate.now(),false));
    }

    public static Optional<BookLoan> bookLoanMock(){
        return Optional.of(new BookLoan(1L, "Felixon", "La cueva de los indios", LocalDate.now(), false));
    }

    public static BookLoanRequest newBookLoanMock(){
        return new BookLoanRequest("Perla", "Una vida extraordinaria", LocalDate.now(),true);
    }
}
