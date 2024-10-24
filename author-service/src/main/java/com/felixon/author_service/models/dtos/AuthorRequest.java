package com.felixon.author_service.models.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorRequest {
    @NotBlank
    @Size(min = 4, max = 16)
    @Column(unique = true)
    private String name;
    @NotBlank
    private String email;
    @NotBlank
    private String biography;
    @NotBlank
    private String nationality;
}
