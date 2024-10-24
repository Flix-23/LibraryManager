package com.felixon.user_service.services;

import com.felixon.user_service.models.dtos.UserEvent;
import com.felixon.user_service.models.dtos.UserRequest;
import com.felixon.user_service.models.dtos.UserResponse;
import com.felixon.user_service.models.entities.Role;
import com.felixon.user_service.models.entities.User;
import com.felixon.user_service.repositories.RoleRepository;
import com.felixon.user_service.repositories.UserRepository;
import com.felixon.user_service.services.publisher.UserToAuthorEvent;
import com.felixon.user_service.services.publisher.UserToOtherEvent;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserToAuthorEvent userToAuthorEvent;

    @Autowired
    private UserToOtherEvent userToOtherEvent;

    private ModelMapper model = new ModelMapper();


    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUser() {

        var users = userRepository.findAll();

        return users.stream().map(this::mapToUserResponse).toList();
    }

    private UserResponse mapToUserResponse(User author) {
        return model.map(author, UserResponse.class);
    }


    @Override
    @Transactional
    public UserResponse addUser(UserRequest userRequest) {
        Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        optionalRoleUser.ifPresent(roles::add);

        var user = model.map(userRequest, User.class);

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.userRepository.save(user);

        userToOtherEvent.publishUser(new UserEvent(user.getUsername()));

        return model.map(user, UserResponse.class);
    }


    @Override
    @Transactional(readOnly = true)
    public UserResponse findUserByName(String username) {
        Optional<User> user = this.userRepository.findByUsername(username);
        return model.map(user, UserResponse.class);
    }

    @Override
    @Transactional
    public UserResponse updateUser(String username, UserRequest userRequest) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        optionalUser.ifPresent(userDB -> {
            userDB.setUsername(userRequest.getUsername());
            userDB.setPassword(passwordEncoder.encode(userRequest.getPassword()));

            if (userRequest.isAuthor()){
                Optional<Role> authorRole = roleRepository.findByName("ROLE_AUTHOR");
                userDB.getRoles().add(authorRole.orElseThrow());

                userToAuthorEvent.publishUserToAuthor(new UserEvent(userRequest.getUsername()));
            }

            this.userRepository.save(userDB);
        });

        return model.map(optionalUser, UserResponse.class);
    }

    @Override
    public UserResponse deleteUser(String username) {
        Optional<User> user = this.userRepository.findByUsername(username);
        user.ifPresent(userRepository::delete);
        return model.map(user, UserResponse.class);
    }


}
