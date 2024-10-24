package com.felixon.book_service.repositories;

import com.felixon.book_service.models.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String title);
    Optional<Book> findByGender(String gender);
    Optional<Book> findByAuthorName(String authorName);
    List<Book> findAll();

}
