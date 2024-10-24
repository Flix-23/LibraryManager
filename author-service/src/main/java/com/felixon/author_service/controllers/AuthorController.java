package com.felixon.author_service.controllers;

import com.felixon.author_service.models.dtos.AuthorRequest;
import com.felixon.author_service.models.dtos.AuthorResponse;
import com.felixon.author_service.services.AuthorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/author")
public class AuthorController {

    @Autowired
    private AuthorServiceImpl authorService;

    @PostMapping("/create")
    public ResponseEntity<AuthorResponse> addAuthor(@RequestBody AuthorRequest authorRequest){

       return ResponseEntity.status(HttpStatus.CREATED).body(authorService.addAuthor(authorRequest));
    }

    @GetMapping
    public List<AuthorResponse> findAllAuthor(){
        return authorService.getAllAuthor();
    }

    @GetMapping("/{name}")
    public ResponseEntity<AuthorResponse> findByName(@PathVariable String name){
        AuthorResponse authorResponse = authorService.findByName(name);

        if (authorResponse != null){
            return ResponseEntity.ok(authorResponse);
        }
         return ResponseEntity.notFound().build();
    }

    @PutMapping("/update/{name}")
    public ResponseEntity<AuthorResponse> updateAuthor(@PathVariable String name, @RequestBody AuthorRequest authorRequest){
        AuthorResponse author = authorService.updateAuthor(name, authorRequest);

        if (author != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(author);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{nameAuthor}")
    public ResponseEntity<AuthorResponse> deleteAuthor(@PathVariable String nameAuthor){
        AuthorResponse author = authorService.deleteAuthor(nameAuthor);

        if (author != null){
            return ResponseEntity.ok(author);
        }
        return ResponseEntity.notFound().build();
    }
}
