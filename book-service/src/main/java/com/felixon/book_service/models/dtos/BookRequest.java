package com.felixon.book_service.models.dtos;

import jakarta.persistence.Column;
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
public class BookRequest {
    @NotBlank
    @Column(unique = true)
    private String title;
    @NotBlank
    private String gender;
    @NotBlank
    private LocalDate datePublication;
    @NotBlank
    @Size(min = 4,max = 16)
    private String authorName;
    @NotBlank
    private boolean available;
}
