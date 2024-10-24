package com.felixon.author_service.services;

import com.felixon.author_service.models.dtos.AuthorRequest;
import com.felixon.author_service.models.dtos.AuthorResponse;

import java.util.List;

public interface AuthorService {
    AuthorResponse addAuthor(AuthorRequest author);
    List<AuthorResponse> getAllAuthor();
    AuthorResponse findByName(String name);
    AuthorResponse updateAuthor(String name, AuthorRequest authorRequest);
    AuthorResponse deleteAuthor(String name);
}
