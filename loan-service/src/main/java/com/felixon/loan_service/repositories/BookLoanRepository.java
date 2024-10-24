package com.felixon.loan_service.repositories;

import com.felixon.loan_service.models.entities.BookLoan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookLoanRepository extends JpaRepository<BookLoan, Long> {
    List<BookLoan> findAll();
}
