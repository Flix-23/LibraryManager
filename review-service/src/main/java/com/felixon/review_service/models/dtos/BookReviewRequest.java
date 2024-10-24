package com.felixon.review_service.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookReviewRequest {
    @NotBlank
    private Long id;
    @NotBlank
    private String bookTitle;
    @NotBlank
    @Size(min = 4, max = 16)
    private String username;
    @NotBlank
    private Integer qualification;
    @NotBlank
    private String comment;
    @NotBlank
    private LocalDate date;
}
