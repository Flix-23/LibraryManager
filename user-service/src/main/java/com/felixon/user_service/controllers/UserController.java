package com.felixon.user_service.controllers;

import com.felixon.user_service.helper.ValidationHelper;
import com.felixon.user_service.models.dtos.UserRequest;
import com.felixon.user_service.models.dtos.UserResponse;
import com.felixon.user_service.models.entities.User;
import com.felixon.user_service.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController extends ValidationHelper {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserResponse> getAllUser(){
        return userService.getAllUser();
    }

    @GetMapping("/{name}")
    public ResponseEntity<UserResponse> findUserByName(@PathVariable String name){
        UserResponse userResponse = userService.findUserByName(name);
        if (userResponse != null){
            return ResponseEntity.ok(userResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> userRegister(@Valid @RequestBody UserRequest user, BindingResult result){
        if(result.hasFieldErrors()){
            return validationError(result);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<?> updateUser(@PathVariable String username, @Valid @RequestBody UserRequest userRequest, BindingResult result){
        if(result.hasFieldErrors()){
            return validationError(result);
        }
        
        UserResponse userResponse = userService.updateUser(username, userRequest);

        if (userResponse != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable String username){
        UserResponse userResponse = userService.deleteUser(username);
        if (userResponse != null){
            return ResponseEntity.ok(userResponse);
        }
        return ResponseEntity.notFound().build();
    }


}
