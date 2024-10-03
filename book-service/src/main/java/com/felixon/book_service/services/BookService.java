package com.felixon.book_service.services;

import com.felixon.book_service.models.dtos.BookRequest;
import com.felixon.book_service.models.dtos.BookResponse;
import com.felixon.book_service.models.entities.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
     void addBook(BookRequest book);
     List<BookResponse> findAllBook();
    Optional<Book> findBookByTitle(String title);
    Optional<Book> findBookByGender(String gender);
    Optional<Book> findBookByAuthor(String authorName);
    Optional<Book> deleteBook(String title);
    Optional<Book> updateBook(String title, BookRequest book);
}
