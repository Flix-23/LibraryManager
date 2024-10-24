package com.felixon.book_service;

import com.felixon.book_service.models.dtos.BookRequest;
import com.felixon.book_service.models.dtos.BookResponse;
import com.felixon.book_service.models.entities.Book;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DataProvider {

    public static List<Book> bookListMock(){
        return List.of(
                new Book(1L, "La cueva de los indios", "aventura", LocalDate.now(), "Felixon", true)
        );
    }

    public static Optional<Book> bookMock(){
        return Optional.of(new Book(1L, "Una vida extraordinaria", "Literatura y ficcion", LocalDate.now(), "Josue", true)
        );
    }

    public static BookRequest newBookMock(){
        return new BookRequest( "Una vida extraordinaria", "Literatura y ficcion", LocalDate.now(), "Josue", true);

    }

    public static List<BookResponse> bookResponsesMock(){
        return List.of(
                new BookResponse(1L, "La cueva de los indios", "aventura", LocalDate.now(), "Felixon", true)
        );
    }
}
