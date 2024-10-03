package com.felixon.book_service.services;

import com.felixon.book_service.models.dtos.BookEvent;
import com.felixon.book_service.models.dtos.BookRequest;
import com.felixon.book_service.models.dtos.BookResponse;
import com.felixon.book_service.repositories.BookRepository;
import lombok.extern.slf4j.Slf4j;
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


    @Transactional
    @Override
    public void addBook(BookRequest bookRequest){
            var book = Book.builder()
                    .title(bookRequest.getTitle())
                    .gender(bookRequest.getGender())
                    .datePublication(bookRequest.getDatePublication())
                    .authorName(AuthorEventConsumer.authorEventName)
                    .available(bookRequest.isAvailable())
                    .build();

        Book bookToEvent = this.bookRepository.save(book);

        log.info("Book added {}", book);

        this.eventPublisher.publishBook(new BookEvent(bookToEvent.getTitle(),
                bookToEvent.getGender(), bookToEvent.getAuthorName()));

    }

    @Transactional(readOnly = true)
    @Override
    public List<BookResponse> findAllBook(){
        var books = bookRepository.findAllBook();

        return books.stream().map(this::mapToBookResponse).toList();
    }

    private BookResponse mapToBookResponse(Book book){
        var bookResponse = BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .gender(book.getGender())
                .datePublication(book.getDatePublication())
                .authorName(book.getAuthorName())
                .available(book.isAvailable())
                .build();

        return bookResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findBookByTitle(String title){

        return this.bookRepository.findByTitle(title);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findBookByGender(String gender) {
        return this.bookRepository.findByGender(gender);
    }

    @Override
    public Optional<Book> findBookByAuthor(String authorName) {
        return this.bookRepository.findByAuthorName(authorName);
    }

    @Override
    @Transactional
    public Optional<Book> updateBook(String title, BookRequest bookRequest) {
        Optional<Book> optionalBook = bookRepository.findByTitle(title);
        if(optionalBook.isPresent()){
            Book bookDB = optionalBook.orElseThrow();
            bookDB.setTitle(bookRequest.getTitle());
            bookDB.setGender(bookRequest.getGender());
            bookDB.setAvailable(bookRequest.isAvailable());
            return Optional.of(bookRepository.save(bookDB));
        }
        return optionalBook;
    }

    @Override
    @Transactional
    public Optional<Book> deleteBook(String title) {
        Optional<Book> optionalBook = bookRepository.findByTitle(title);

        optionalBook.ifPresent(bookRepository::delete);

        return optionalBook;
    }


}
