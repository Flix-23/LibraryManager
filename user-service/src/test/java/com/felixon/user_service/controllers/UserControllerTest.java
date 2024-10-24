package com.felixon.user_service.controllers;

import com.felixon.user_service.models.dtos.UserRequest;
import com.felixon.user_service.models.dtos.UserResponse;
import com.felixon.user_service.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    private UserRequest userRequest;

    private UserResponse userResponse;

    @BeforeEach
    void setup(){
        userRequest = UserRequest.builder()
                .username("Felixon")
                .password("1234")
                .registrationDate(LocalDate.now())
                .enable(true)
                .author(false)
                .roles(new HashSet<>())
                .build();
        ModelMapper model = new ModelMapper();
        userResponse = model.map(userRequest, UserResponse.class);
    }

    @DisplayName("Test user save")
    @Test
    @WithMockUser(username = "Felixon", password = "123445334")
    void testSaveUser() throws Exception{
        when(userService.addUser(userRequest)).thenReturn(userResponse);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"Felixon\", " +
                                "\"password\":\"123443434\" }"))
                        .andExpect(status().isCreated());
    }

    @DisplayName("Test find all users")
    @Test
    @WithMockUser(username = "Felixon", password = "1234")
    void testFindAllUser() throws Exception{
        when(userService.getAllUser()).thenReturn(List.of(userResponse));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Test find book user by name")
    @Test
    @WithMockUser(username = "Felixon", password = "1234")
    void testFindByName_whenUserExist() throws Exception{
        when(userService.findUserByName("Felixon")).thenReturn(userResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/Felixon")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(userRequest.getUsername()));
    }

}
