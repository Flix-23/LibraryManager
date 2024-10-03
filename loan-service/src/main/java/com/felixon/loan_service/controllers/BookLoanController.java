package com.felixon.loan_service.controllers;

import com.felixon.loan_service.models.dtos.BookLoanRequest;
import com.felixon.loan_service.models.dtos.BookLoanResponse;
import com.felixon.loan_service.models.entities.BookLoan;
import com.felixon.loan_service.services.BookLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/loan")
public class BookLoanController {

    @Autowired
    private BookLoanService loanService;

    @GetMapping
    public List<BookLoanResponse> getAllLoan(){
        return loanService.getAllLoan();
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void addLoan(@RequestBody BookLoanRequest bookLoan){
        this.loanService.addLoan(bookLoan);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookLoan> findLoanById(@PathVariable Long idLoan){
        Optional<BookLoan> optionalBookLoan = loanService.findById(idLoan);

        if (optionalBookLoan.isPresent()){
            return ResponseEntity.ok(optionalBookLoan.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/update/{idLoan}")
    public ResponseEntity<?> updateLoan(@PathVariable Long idLoan, @RequestBody BookLoanRequest bookLoanRequest){
        Optional<BookLoan> optionalBookLoan = loanService.updateLoan(idLoan, bookLoanRequest);
        if (optionalBookLoan.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(optionalBookLoan.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }
}
