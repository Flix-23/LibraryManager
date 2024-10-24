package com.felixon.loan_service.models.dtos;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private Long id;
    @NotBlank
    private String username;
    @NotBlank
    private String bookTitle;
    @NotBlank
    private LocalDate loanDate;
    @NotBlank
    private boolean returned;
}
