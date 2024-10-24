package com.felixon.user_service.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.felixon.user_service.DataProvider;
import com.felixon.user_service.models.dtos.UserEvent;
import com.felixon.user_service.models.dtos.UserRequest;
import com.felixon.user_service.models.dtos.UserResponse;
import com.felixon.user_service.models.entities.Role;
import com.felixon.user_service.models.entities.User;
import com.felixon.user_service.repositories.RoleRepository;
import com.felixon.user_service.repositories.UserRepository;
import com.felixon.user_service.services.publisher.UserToOtherEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserToOtherEvent userToOtherEvent;


    @InjectMocks
    private UserServiceImpl userService;

    private UserRequest userRequest;

    private UserResponse userResponse;

    private final String username = "Felixon";

    @BeforeEach
    void setUp(){
        userRequest = UserRequest.builder()
                .username("Felixon")
                .password("1234")
                .registrationDate(LocalDate.now())
                .enable(true)
                .author(false)
                .roles(Set.of(new Role(1L, "ROLE_USER")))
                .build();
        ModelMapper model = new ModelMapper();
        userResponse = model.map(userRequest, UserResponse.class);

    }

    @DisplayName("Test to save user")
    @Test
    void testUserSave(){

        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(DataProvider.getRoleMock()));
        when(passwordEncoder.encode(anyString())).thenReturn(userRequest.getPassword());
        when(userRepository.save(any(User.class))).thenReturn(DataProvider.userMock());

        UserResponse result = userService.addUser(userRequest);

        assertNotNull(result);
        assertEquals("Felixon", result.getUsername());

        verify(roleRepository, times(1)).findByName("ROLE_USER");
        verify(passwordEncoder, times(1)).encode(userRequest.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userToOtherEvent, times(1)).publishUser(any(UserEvent.class));
    }

    @DisplayName("Test find all users")
    @Test
    void testGetAllUser(){
        when(userRepository.findAll()).thenReturn(DataProvider.userListMock());
        List<UserResponse> users = userService.getAllUser();

        assertNotNull(users);
        assertFalse(users.isEmpty());
        assertEquals("Felixon", users.get(0).getUsername());

        verify(this.userRepository).findAll();
    }

    @ DisplayName("Test find user by name")
    @Test
    void testFindByName_whenUserExist(){

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(DataProvider.userMock()));
        UserResponse userFound = userService.findUserByName(username);

        assertNotNull(userFound);
        assertEquals(username, userFound.getUsername());
        verify(this.userRepository).findByUsername(anyString());
    }

    @DisplayName("Test not found user by name")
    @Test
    void testFindByName_whenUserDoesNotExist(){

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        UserResponse userFound = userService.findUserByName(username);

        assertNull(userFound);
        verify(this.userRepository).findByUsername(anyString());
    }

}
