package com.felixon.user_service.services;

import com.felixon.user_service.models.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUser();
    User addUser(User user);
    Optional<User> findUserByName(String Username);
    Optional<User> updateUser(String username, User user);
}
