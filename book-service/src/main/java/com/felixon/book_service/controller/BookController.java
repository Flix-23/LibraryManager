package com.felixon.book_service.controller;

import com.felixon.book_service.models.dtos.BookRequest;
import com.felixon.book_service.models.dtos.BookResponse;
import com.felixon.book_service.services.BookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
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

    @GetMapping("/title/{title}")
    @ResponseStatus(HttpStatus.OK)
    public BookResponse findBookByTitle(@PathVariable String title){
        return this.bookService.findBookByTitle(title);
    }

    @GetMapping("/gender/{gender}")
    @ResponseStatus(HttpStatus.OK)
    public BookResponse findBookByGender(@PathVariable String gender){
        return this.bookService.findBookByGender(gender);
    }

    @GetMapping("/author/{author}")
    @ResponseStatus(HttpStatus.OK)
    public BookResponse findBookByAuthorName(@PathVariable String author){
        return this.bookService.findBookByAuthor(author);
    }


    @PostMapping("/create")
    public ResponseEntity<BookResponse> addBook(@RequestBody BookRequest book){

        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.addBook(book));
    }

    @PutMapping("/update/{title}")
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse updateBook(@RequestBody BookRequest bookRequest, @PathVariable String title){
        return this.bookService.updateBook(title, bookRequest);
    }

    @DeleteMapping("/delete/{title}")
    @ResponseStatus(HttpStatus.OK)
    public BookResponse deleteBook(@PathVariable String title){
        return this.bookService.deleteBook(title);
    }

}
