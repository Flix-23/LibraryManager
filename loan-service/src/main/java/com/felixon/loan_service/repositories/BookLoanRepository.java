package com.felixon.loan_service.repositories;

import com.felixon.loan_service.models.entities.BookLoan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookLoanRepository extends CrudRepository<BookLoan, Long> {
    @Query("SELECT b FROM BookLoan b")
    List<BookLoan> findAllLoan();
}
