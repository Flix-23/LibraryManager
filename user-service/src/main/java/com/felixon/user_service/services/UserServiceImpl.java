package com.felixon.user_service.services;

import com.felixon.user_service.models.dtos.UserEvent;
import com.felixon.user_service.models.entities.Role;
import com.felixon.user_service.models.entities.User;
import com.felixon.user_service.repositories.RoleRepository;
import com.felixon.user_service.repositories.UserRepository;
import com.felixon.user_service.services.event_publication.UserToAuthorEvent;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
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
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Autowired
    private UserToAuthorEvent userToAuthorEvent;


    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUser() {

        return (List<User>) userRepository.findAll();
    }

    @Override
    @Transactional
    public User addUser(User user) {
            Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
            Set<Role> roles = new HashSet<>();

            optionalRoleUser.ifPresent(roles::add);


            user.setRoles(roles);
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            User userToEvent = this.userRepository.save(user);

            var event = new UserEvent(userToEvent.getId(), userToEvent.getUsername());

            publishUser(event);

            return userToEvent;
    }

    public void publishUser(UserEvent event){
        kafkaTemplate.send("user-topic", event);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserByName(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public Optional<User> updateUser(String username, User user) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        Set<Role> roles = new HashSet<>();

        optionalUser.ifPresent(userDB -> {
            userDB.setUsername(user.getUsername());
            userDB.setPassword(passwordEncoder.encode(user.getPassword()));

            if (user.isAuthor()){
                Optional<Role> authorRole = roleRepository.findByName("ROLE_AUTHOR");
                userDB.getRoles().add(authorRole.orElseThrow());

                var event = new UserEvent(user.getId(), user.getUsername());
                userToAuthorEvent.publishUserToAuthor(event);
            }

            this.userRepository.save(userDB);
        });

        return optionalUser;
    }



}
