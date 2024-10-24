package com.felixon.author_service.controllers;

import ch.qos.logback.core.model.ImplicitModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.felixon.author_service.DataProvider;
import com.felixon.author_service.models.dtos.AuthorResponse;
import com.felixon.author_service.models.entities.Author;
import com.felixon.author_service.services.AuthorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorServiceImpl authorService;

    @Autowired
    private ObjectMapper objectMapper;

    private Author authorToResponse;
    private AuthorResponse author;

    @BeforeEach
    void setup(){
        authorToResponse = Author.builder()
                .name("Felixon")
                .email("michaelm23@gmail.com")
                .biography("nacio...")
                .nationality("dominicano")
                .build();

        ModelMapper model = new ModelMapper();
        author = model.map(authorToResponse, AuthorResponse.class);
    }

    @Test
    void testSaveAuthor() throws Exception {

        when(authorService.addAuthor(DataProvider.newAuthorMock())).thenReturn(author);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/author/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"felixon\", \"email\":" +
                        "\"michaelm23@gmail.com\", \"biography\"" +
                        ":\"nacio...\"," + " \"nationality\":\"dominicano\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Felixon"))
                .andExpect(jsonPath("$.email").value("michaelm23@gmail.com"));

    }

    @Test
    void testFindByName_whenAuthorExist() throws Exception{
        when(authorService.findByName(author.getName())).thenReturn(author);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/author/Felixon")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Felixon"));
    }

    @Test
    void testNoFoundName_whenAuthorDoesNotExist() throws Exception{
        when(authorService.findByName(author.getName())).thenReturn(author);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/author/Felix")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
    }

    @Test
    void testFindAllAuthor() throws Exception{
        when(authorService.getAllAuthor()).thenReturn(DataProvider.authorResponseListMock());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/author")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
    }

    @Test
    void testUpdateAuthor_whenAuthorExist() throws Exception{
        when(authorService.updateAuthor("Felixon", DataProvider.newAuthorMock())).thenReturn(author);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/author/update/Felixon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"felixon\", \"email\":" +
                        "\"michaelm23@gmail.com\", \"biography\"" +
                        ":\"nacio...\"," + " \"nationality\":\"dominicano\"}"))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.name").value("Felixon"));


    }

    @Test
    void testUpdateAuthor_whenAuthorDoesNotExist() throws Exception{
        when(authorService.updateAuthor(author.getName(), DataProvider.newAuthorMock())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/author/update/Felixon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"felixon\", \"email\":" +
                                "\"michaelm23@gmail.com\", \"biography\"" +
                                ":\"nacio...\"," + " \"nationality\":\"dominicano\"}"))
                        .andExpect(status().isNotFound());

    }

    @Test
    void testDeleteAuthor_whenAuthorExist() throws Exception{
        when(authorService.deleteAuthor(author.getName())).thenReturn(author);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/author/delete/Felixon")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.name").value("Felixon"));

    }

    @Test
    void testDeleteAuthor_whenAuthorDoesNotExist() throws Exception{
        when(authorService.deleteAuthor(author.getName())).thenReturn(author);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/author/delete/Felix")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());

    }


}
