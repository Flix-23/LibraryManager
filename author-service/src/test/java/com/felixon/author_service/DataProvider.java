package com.felixon.author_service;

import com.felixon.author_service.models.dtos.AuthorRequest;
import com.felixon.author_service.models.dtos.AuthorResponse;
import com.felixon.author_service.models.entities.Author;

import java.util.List;
import java.util.Optional;

public class DataProvider {

    public static List<Author> authorListMock(){
        return  List.of(
                new Author(1L, "Felixon", "felixon@gmail.com", "nacio...", "dominicano"),
                new Author(2L, "Josue Meran", "jmeran@gmail.com", "nacio...", "dominicano"),
                new Author(3L, "Miguel Angel", "migang23@gmail.com", "nacio...", "dominicano"),
                new Author(4L, "Perla Pimentel", "pminentelperla@gmail.com", "nacio...", "dominicano"),
                new Author(5L, "Carmen", "carmen23142@gmail.com", "nacio...", "dominicano")
        );

    }

    public static List<AuthorResponse> authorResponseListMock() {
        return List.of(
                new AuthorResponse(1L, "Felixon", "felixon@gmail.com", "nacio...", "dominicano"),
                new AuthorResponse(2L, "Josue Meran", "jmeran@gmail.com", "nacio...", "dominicano"),
                new AuthorResponse(3L, "Miguel Angel", "migang23@gmail.com", "nacio...", "dominicano"),
                new AuthorResponse(4L, "Perla Pimentel", "pminentelperla@gmail.com", "nacio...", "dominicano"),
                new AuthorResponse(5L, "Carmen", "carmen23142@gmail.com", "nacio...", "dominicano")
        );
    }
    public static Optional<Author> authorMock(){
        return Optional.of(new Author(1L, "Felixon", "felixon@gmail.com", "nacio...", "dominicano"));
    }

    public static AuthorRequest newAuthorMock(){
        return new AuthorRequest("felixon", "michaelm23@gmail.com", "nacio...", "dominicano");
    }
}
