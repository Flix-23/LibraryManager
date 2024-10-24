package com.felixon.book_service.services;

import com.felixon.book_service.models.dtos.BookRequest;
import com.felixon.book_service.models.dtos.BookResponse;

import java.util.List;

public interface BookService {
     BookResponse addBook(BookRequest book);
     List<BookResponse> findAllBook();
    BookResponse findBookByTitle(String title);
    BookResponse findBookByGender(String gender);
    BookResponse findBookByAuthor(String authorName);
    BookResponse deleteBook(String title);
    BookResponse updateBook(String title, BookRequest book);
}
