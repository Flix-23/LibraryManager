package com.felixon.author_service.controllers;

import com.felixon.author_service.models.dtos.AuthorRequest;
import com.felixon.author_service.models.dtos.AuthorResponse;
import com.felixon.author_service.services.AuthorServiceImpl;
import com.felixon.author_service.models.entities.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/author")
public class AuthorController {

    @Autowired
    private AuthorServiceImpl authorService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void addAuthor(@RequestBody AuthorRequest authorRequest){
        this.authorService.addAuthor(authorRequest);
    }

    @GetMapping
    public List<AuthorResponse> findAllAuthor(){
        return authorService.getAllAuthor();
    }

    @GetMapping("/{name}")
    public ResponseEntity<Author> findByName(@PathVariable String name){
        Optional<Author> optionalAuthor = authorService.findByName(name);

        if (optionalAuthor.isPresent()){
            return ResponseEntity.ok(optionalAuthor.orElseThrow());
        }
         return ResponseEntity.notFound().build();
    }

    @PutMapping("/update/{name}")
    public ResponseEntity<?> updateAuthor(@PathVariable String name, @RequestBody AuthorRequest authorRequest){
        Optional<Author> optionalAuthor = authorService.updateAuthor(name, authorRequest);

        if (optionalAuthor.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(optionalAuthor.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{nameAuthor}")
    public ResponseEntity<Author> deleteAuthor(@PathVariable String nameAuthor){
        Optional<Author> optionalAuthor = authorService.deleteAuthor(nameAuthor);

        if (optionalAuthor.isPresent()){
            return ResponseEntity.ok(optionalAuthor.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }
}
