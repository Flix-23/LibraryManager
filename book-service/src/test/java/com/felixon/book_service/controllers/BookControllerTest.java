package com.felixon.book_service.controllers;

import com.felixon.book_service.DataProvider;
import com.felixon.book_service.controller.BookController;
import com.felixon.book_service.models.dtos.BookRequest;
import com.felixon.book_service.models.dtos.BookResponse;
import com.felixon.book_service.models.entities.Book;
import com.felixon.book_service.services.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookServiceImpl bookService;

    private BookRequest book;

    private BookResponse bookResponse;

    @BeforeEach
    void setup(){
        book  = BookRequest.builder()
                .title("Una vida extraordinaria")
                .gender("Literatura y ficcion")
                .datePublication(LocalDate.now())
                .authorName("Josue")
                .available(true)
                .build();

        ModelMapper model = new ModelMapper();
        bookResponse = model.map(book, BookResponse.class);
    }

    @DisplayName("Test bookController save")
    @Test
    void testSaveBook() throws Exception{
        when(bookService.addBook(DataProvider.newBookMock())).thenReturn(bookResponse);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/book/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Una vida extraordinaria\", " +
                                "\"gender\":\"Literatura y ficcion\", " +
                                "\"datePublication\":\"" + LocalDate.now() + "\", " +
                                "\"authorName\":\"Josue\", \"available\":true}"))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.title").value("Una vida extraordinaria"));

    }

    @DisplayName("Test find all book")
    @Test
    void testFindAll() throws Exception{
        when(bookService.findAllBook()).thenReturn(DataProvider.bookResponsesMock());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/book")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Test find book by title")
    @Test
    void testFindByTitle_whenBookExist() throws Exception{
        when(bookService.findBookByTitle(book.getTitle())).thenReturn(bookResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/book/title/Una vida extraordinaria")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.title").value(book.getTitle()));
    }


    @DisplayName("Test find book by gender")
    @Test
    void testFindByGender_whenBookExist() throws Exception{
        when(bookService.findBookByGender(book.getGender())).thenReturn(bookResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/book/gender/Literatura y ficcion")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.gender").value(book.getGender()));
    }

    @DisplayName("Test find book by author name")
    @Test
    void testFindByAuthor_whenBookExist() throws Exception{
        when(bookService.findBookByAuthor(book.getAuthorName())).thenReturn(bookResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/book/author/Josue")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.authorName").value(book.getAuthorName()));
    }

    @DisplayName("Test update book")
    @Test
    void testUpdateAuthor_whenAuthorExist() throws Exception{
        when(bookService.updateBook(book.getTitle(), DataProvider.newBookMock())).thenReturn(bookResponse);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/book/update/Una vida extraordinaria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Una vida extraordinaria\", " +
                                "\"gender\":\"Literatura y ficcion\", " +
                                "\"datePublication\":\"" + LocalDate.now() + "\", " +
                                "\"authorName\":\"Josue\", \"available\":true}"))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.title").value(book.getTitle()));


    }

    @DisplayName("Test delete book")
    @Test
    void testDeleteAuthor_whenAuthorExist() throws Exception{
        when(bookService.deleteBook(book.getTitle())).thenReturn(bookResponse);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/book/delete/Una vida extraordinaria")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.title").value(book.getTitle()));

    }



}
