package com.felixon.author_service.services;

import com.felixon.author_service.models.dtos.AuthorRequest;
import com.felixon.author_service.models.dtos.AuthorResponse;
import com.felixon.author_service.models.entities.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    void addAuthor(AuthorRequest author);
    List<AuthorResponse> getAllAuthor();
    Optional<Author> findByName(String name);
    Optional<Author> updateAuthor(String name, AuthorRequest authorRequest);
    Optional<Author> deleteAuthor(String name);
}
