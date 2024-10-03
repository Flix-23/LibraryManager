package com.felixon.loan_service.services;

import com.felixon.loan_service.models.dtos.BookLoanRequest;
import com.felixon.loan_service.models.dtos.BookLoanResponse;
import com.felixon.loan_service.models.entities.BookLoan;

import java.util.List;
import java.util.Optional;

public interface BookLoanService {
    void addLoan(BookLoanRequest bookLoanRequest);
    List<BookLoanResponse> getAllLoan();
    Optional<BookLoan> findById(Long idLoan);
    Optional<BookLoan> updateLoan(Long idLoan, BookLoanRequest bookLoanRequest);

}

