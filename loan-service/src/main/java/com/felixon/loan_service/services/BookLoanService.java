package com.felixon.loan_service.services;

import com.felixon.loan_service.models.dtos.BookLoanRequest;
import com.felixon.loan_service.models.dtos.BookLoanResponse;

import java.util.List;

public interface BookLoanService {
    BookLoanResponse addLoan(BookLoanRequest bookLoanRequest);
    List<BookLoanResponse> getAllLoan();
    BookLoanResponse findById(Long idLoan);
    BookLoanResponse updateLoan(Long idLoan, BookLoanRequest bookLoanRequest);

}

