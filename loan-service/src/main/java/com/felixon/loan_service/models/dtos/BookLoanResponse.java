package com.felixon.loan_service.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookLoanResponse {
    private Long id;
    private String username;
    private String bookTitle;
    private LocalDate loanDate;
    private boolean returned;
}
