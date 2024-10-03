package com.felixon.review_service.models.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookReviewResponse {
    private Long id;
    private String bookTitle;
    private String username;
    private Integer qualification;
    private String comment;
    private LocalDate date;
}
