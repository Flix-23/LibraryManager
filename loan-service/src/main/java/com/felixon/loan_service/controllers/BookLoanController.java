package com.felixon.loan_service.controllers;

import com.felixon.loan_service.models.dtos.BookLoanRequest;
import com.felixon.loan_service.models.dtos.BookLoanResponse;
import com.felixon.loan_service.services.BookLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loan")
public class BookLoanController {

    @Autowired
    private BookLoanService loanService;

    @GetMapping
    public List<BookLoanResponse> getAllLoan(){
        return loanService.getAllLoan();
    }

    @PostMapping("/create")
    public ResponseEntity<BookLoanResponse> addLoan(@RequestBody BookLoanRequest bookLoan){

        return ResponseEntity.status(HttpStatus.CREATED).body(loanService.addLoan(bookLoan));

    }

    @GetMapping("/{idLoan}")
    public ResponseEntity<BookLoanResponse> findLoanById(@PathVariable Long idLoan){
        BookLoanResponse bookLoanResponse = loanService.findById(idLoan);

        if (bookLoanResponse != null){
            return ResponseEntity.ok(bookLoanResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/update/{idLoan}")
    public ResponseEntity<?> updateLoan(@PathVariable Long idLoan, @RequestBody BookLoanRequest bookLoanRequest){
        BookLoanResponse bookLoanResponse = loanService.updateLoan(idLoan, bookLoanRequest);
        if (bookLoanResponse != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(bookLoanResponse);
        }
        return ResponseEntity.notFound().build();
    }
}
