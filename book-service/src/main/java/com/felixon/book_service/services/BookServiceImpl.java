package com.felixon.book_service.services;

import com.felixon.book_service.models.dtos.BookEvent;
import com.felixon.book_service.models.dtos.BookRequest;
import com.felixon.book_service.models.dtos.BookResponse;
import com.felixon.book_service.repositories.BookRepository;
import com.felixon.book_service.services.consumer.AuthorEventConsumer;
import com.felixon.book_service.services.publisher.BookEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.felixon.book_service.models.entities.Book;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookServiceImpl implements BookService{

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookEventPublisher eventPublisher;

    private ModelMapper model = new ModelMapper();


    @Transactional
    @Override
    public BookResponse addBook(BookRequest bookRequest){
        bookRequest.setAuthorName(AuthorEventConsumer.authorEventName);
        var book = model.map(bookRequest, Book.class);

        this.bookRepository.save(book);

        this.eventPublisher.publishBook(new BookEvent(book.getTitle(),
                book.getGender(), book.getAuthorName()));

        return model.map(book, BookResponse.class);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookResponse> findAllBook(){
        var books = bookRepository.findAll();

        return books.stream().map(this::mapToBookResponse).toList();
    }

    private BookResponse mapToBookResponse(Book book){
        return model.map(book, BookResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse findBookByTitle(String title){
        Optional<Book> book = this.bookRepository.findByTitle(title);
        return model.map(book, BookResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse findBookByGender(String gender) {
        Optional<Book> book = this.bookRepository.findByGender(gender);
        return model.map(book, BookResponse.class);
    }

    @Override
    public BookResponse findBookByAuthor(String authorName) {
        Optional<Book> book = this.bookRepository.findByAuthorName(authorName);
        return model.map(book, BookResponse.class);
    }

    @Override
    @Transactional
    public BookResponse updateBook(String title, BookRequest bookRequest) {
        Optional<Book> optionalBook = bookRepository.findByTitle(title);
        if(optionalBook.isPresent()){
            Book bookDB = optionalBook.orElseThrow();
            bookDB.setTitle(bookRequest.getTitle());
            bookDB.setGender(bookRequest.getGender());
            bookDB.setAvailable(bookRequest.isAvailable());
            bookRepository.save(bookDB);
        }
        return model.map(optionalBook, BookResponse.class);
    }

    @Override
    @Transactional
    public BookResponse deleteBook(String title) {
        Optional<Book> optionalBook = bookRepository.findByTitle(title);

        optionalBook.ifPresent(bookRepository::delete);

        return model.map(optionalBook, BookResponse.class);
    }


}
