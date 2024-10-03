package com.felixon.author_service.services;

import com.felixon.author_service.models.dtos.AuthorEvent;
import com.felixon.author_service.models.dtos.AuthorRequest;
import com.felixon.author_service.models.dtos.AuthorResponse;
import com.felixon.author_service.models.entities.Author;
import com.felixon.author_service.repositories.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService{

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorEventPublisher authorEventPublisher;


    @Override
    @Transactional
    public void addAuthor(AuthorRequest authorRequest){
            var author = Author.builder()
                    .name(UserConsumer.userEventName)
                    .email(authorRequest.getEmail())
                    .biography(authorRequest.getBiography())
                    .nationality(authorRequest.getNationality())
                    .build();

        Author authorToEvent = this.authorRepository.save(author);
        log.info("Author added {}", author);

        this.authorEventPublisher.publishAuthorEvent(new AuthorEvent(UserConsumer.userEventName,
                authorToEvent.getEmail(), authorToEvent.getBiography(),
                authorToEvent.getNationality()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorResponse> getAllAuthor() {
        var authors = authorRepository.findAllAuthor();


        return authors.stream().map(this::mapToAuthorResponse).toList();
    }

    private AuthorResponse mapToAuthorResponse(Author author) {
        return AuthorResponse.builder()
                .id(author.getId())
                .name(author.getName())
                .email(author.getEmail())
                .biography(author.getBiography())
                .nationality(author.getNationality())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Author> findByName(String name) {
        return this.authorRepository.findByName(name);
    }

    @Override
    @Transactional
    public Optional<Author> updateAuthor(String name, AuthorRequest authorRequest) {
        Optional<Author> optionalAuthor = authorRepository.findByName(name);

        if(optionalAuthor.isPresent()){
            Author authorDB = optionalAuthor.orElseThrow();
            authorDB.setEmail(authorRequest.getEmail());
            authorDB.setBiography(authorRequest.getBiography());
            authorDB.setNationality(authorRequest.getNationality());
            return Optional.of(authorRepository.save(authorDB));
        }
        return optionalAuthor;
    }

    @Override
    @Transactional
    public Optional<Author> deleteAuthor(String name) {
        Optional<Author> optionalAuthor = authorRepository.findByName(name);

        optionalAuthor.ifPresent(authorRepository::delete);

        return optionalAuthor;
    }

}
