package com.felixon.book_service.repositories;

import com.felixon.book_service.models.dtos.BookResponse;
import com.felixon.book_service.models.entities.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
    Optional<Book> findByTitle(String title);
    Optional<Book> findByGender(String gender);
    Optional<Book> findByAuthorName(String authorName);
    @Query("SELECT b FROM Book b")
    List<Book> findAllBook();

}
