package com.felixon.user_service.controllers;

import com.felixon.user_service.helper.ValidationHelper;
import com.felixon.user_service.models.entities.User;
import com.felixon.user_service.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController extends ValidationHelper {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUser(){
        return userService.getAllUser();
    }

    @GetMapping("/{name}")
    public ResponseEntity<User> findUserByName(@PathVariable String name){
        Optional<User> optionalUser = userService.findUserByName(name);
        if (optionalUser.isPresent()){
            return ResponseEntity.ok(optionalUser.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> userRegister(@Valid @RequestBody User user, BindingResult result){
        if(result.hasFieldErrors()){
            return validationError(result);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<?> updateUser(@PathVariable String username, @Valid @RequestBody User user, BindingResult result){
        if(result.hasFieldErrors()){
            return validationError(result);
        }
        
        Optional<User> optionalUser = userService.updateUser(username, user);

        if (optionalUser.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(optionalUser.orElseThrow());
        }

        return ResponseEntity.notFound().build();
    }


}
