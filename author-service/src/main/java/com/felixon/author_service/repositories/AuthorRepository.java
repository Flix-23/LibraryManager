package com.felixon.author_service.repositories;

import com.felixon.author_service.models.entities.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends CrudRepository<Author,Long> {
    Optional<Author> findByName(String authorName);
    @Query("SELECT a FROM Author a")
    List<Author> findAllAuthor();
}
