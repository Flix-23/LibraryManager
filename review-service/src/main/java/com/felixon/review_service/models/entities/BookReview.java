package com.felixon.review_service.models.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "book_reviews")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookReview {

    @Transient
    public static final String SEQUENCE_NAME = "reviews_sequence";

    @Id
    private Long id;

    @NotBlank
    private String bookTitle;
    @NotBlank
    private String username;
    @Size(min = 1, max = 5)
    private Integer qualification;
    private String comment;
    private LocalDate date = LocalDate.now();


    @Override
    public String toString() {
        return "BookReview{" +
                "id=" + id +
                ", bookTitle='" + bookTitle + '\'' +
                ", username='" + username + '\'' +
                ", qualification=" + qualification +
                ", comment='" + comment + '\'' +
                ", date=" + date +
                '}';
    }
}
