package com.felixon.user_service.services;

import com.felixon.user_service.models.dtos.UserRequest;
import com.felixon.user_service.models.dtos.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUser();
    UserResponse addUser(UserRequest userRequest);
    UserResponse findUserByName(String Username);
    UserResponse updateUser(String username, UserRequest userRequest);
    UserResponse deleteUser(String username);
}
