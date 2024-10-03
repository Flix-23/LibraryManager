package com.felixon.book_service.controller;

import com.felixon.book_service.models.dtos.BookRequest;
import com.felixon.book_service.models.dtos.BookResponse;
import com.felixon.book_service.models.entities.Book;
import com.felixon.book_service.services.BookServiceImpl;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private BookServiceImpl bookService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookResponse> getAllBook(){
        return this.bookService.findAllBook();
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    private List<BookResponse> getAllBookFallBack(Throwable throwable){
        return Collections.emptyList();
    }

    @GetMapping("/{title}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Book> findBookByTitle(@PathVariable String title){
        return this.bookService.findBookByTitle(title);
    }

    @GetMapping("/{gender}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Book> findBookByGender(@PathVariable String gender){
        return this.bookService.findBookByGender(gender);
    }

    @GetMapping("/{author}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Book> findBookByAuthorName(@PathVariable String author){
        return this.bookService.findBookByAuthor(author);
    }


    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void addBook(@RequestBody BookRequest book){
        this.bookService.addBook(book);
    }

    @PutMapping("/update/{title}")
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<Book> updateBook(@RequestBody BookRequest bookRequest, @PathVariable String title){
        return this.bookService.updateBook(title, bookRequest);
    }

    @DeleteMapping("/delete/{title}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Book> deleteBook(@PathVariable String title){
        return this.bookService.deleteBook(title);
    }

}
