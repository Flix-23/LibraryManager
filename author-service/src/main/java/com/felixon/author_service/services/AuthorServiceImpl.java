package com.felixon.author_service.services;

import com.felixon.author_service.models.dtos.AuthorEvent;
import com.felixon.author_service.models.dtos.AuthorRequest;
import com.felixon.author_service.models.dtos.AuthorResponse;
import com.felixon.author_service.models.entities.Author;
import com.felixon.author_service.repositories.AuthorRepository;
import com.felixon.author_service.services.consumer.UserConsumer;
import com.felixon.author_service.services.publisher.AuthorEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

    private ModelMapper model = new ModelMapper();


    @Override
    @Transactional
    public AuthorResponse addAuthor(AuthorRequest authorRequest){

        authorRequest.setName(UserConsumer.userEventName);
        var author = model.map(authorRequest, Author.class);

        this.authorRepository.save(author);

        this.authorEventPublisher.publishAuthorEvent(
                new AuthorEvent(author.getName(),
                author.getEmail(), author.getBiography(),
                author.getNationality())
        );



        return model.map(author, AuthorResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorResponse> getAllAuthor() {
        var authors = authorRepository.findAll();

        return authors.stream().map(this::mapToAuthorResponse).toList();
    }

    private AuthorResponse mapToAuthorResponse(Author author) {
        return model.map(author, AuthorResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorResponse findByName(String name) {
         Optional<Author> author= this.authorRepository.findByName(name);

        return model.map(author, AuthorResponse.class);
    }

    @Override
    @Transactional
    public AuthorResponse updateAuthor(String name, AuthorRequest authorRequest) {
        Optional<Author> optionalAuthor = authorRepository.findByName(name);

        if(optionalAuthor.isPresent()){
            Author authorDB = optionalAuthor.orElseThrow();
            authorDB.setEmail(authorRequest.getEmail());
            authorDB.setBiography(authorRequest.getBiography());
            authorDB.setNationality(authorRequest.getNationality());
            authorRepository.save(authorDB);
        }

        return model.map(optionalAuthor, AuthorResponse.class);
    }

    @Override
    @Transactional
    public AuthorResponse deleteAuthor(String name) {

        Optional<Author> optionalAuthor = authorRepository.findByName(name);

        optionalAuthor.ifPresent(authorRepository::delete);

        return model.map(optionalAuthor, AuthorResponse.class);
    }

}
