package com.felixon.user_service.models.dtos;


import com.felixon.user_service.models.entities.Role;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    @NotBlank
    private Long id;

    @NotBlank
    @Column(unique = true)
    @Size(min = 4,max = 30)
    private String username;
    private LocalDate registrationDate;
    private boolean enable;

    private Set<Role> roles;
}
