package com.felixon.book_service.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "book")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String title;
    @NotBlank
    private String gender;
    @Column(name = "publication_date")
    private LocalDate datePublication;
    @Column(name = "author_name")
    private String authorName;
    private boolean available;

    @PrePersist
    public void prePersistBook(){
        datePublication = LocalDate.now();
        available = true;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", gender='" + gender + '\'' +
                ", datePublication=" + datePublication +
                ", authorName='" + authorName + '\'' +
                ", available=" + available +
                '}';
    }
}
