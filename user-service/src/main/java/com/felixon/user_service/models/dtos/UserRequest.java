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
public class UserRequest {

    @NotBlank
    @Column(unique = true)
    @Size(min = 4,max = 30)
    private String username;
    @NotBlank
    @Size(min = 4)
    private String password;

    private LocalDate registrationDate;

    private boolean enable;

    private boolean author;
    private Set<Role> roles;

}
