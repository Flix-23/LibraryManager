package com.felixon.author_service.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "author")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotBlank
    @Size(min = 4, max = 16)
    private String name;
    @NotBlank
    private String email;
    @NotBlank
    private String biography;
    @NotBlank
    private String nationality;


    @Override
    public String toString() {
        return "Author{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", biography='" + biography + '\'' +
                ", nationality='" + nationality + '\'' +
                '}';
    }
}
