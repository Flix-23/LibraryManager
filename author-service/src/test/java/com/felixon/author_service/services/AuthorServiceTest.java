package com.felixon.author_service.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.felixon.author_service.DataProvider;
import com.felixon.author_service.models.dtos.AuthorEvent;
import com.felixon.author_service.models.dtos.AuthorRequest;
import com.felixon.author_service.models.dtos.AuthorResponse;
import com.felixon.author_service.models.entities.Author;
import com.felixon.author_service.repositories.AuthorRepository;
import com.felixon.author_service.services.publisher.AuthorEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Mock
    private AuthorEventPublisher authorEventPublisher;

    private AuthorRequest authorRequest;

    private final String name = "Felixon";

    @BeforeEach
    void setup(){
        authorRequest = new AuthorRequest();
        authorRequest.setEmail("flixm3422@gmail.com");
        authorRequest.setBiography("nacio...");
        authorRequest.setNationality("dominicano");
    }

    @DisplayName("Author save test")
    @Test
    void testAuthorSave_whenAuthorExist(){
       authorRequest = DataProvider.newAuthorMock();

        Author authorToSave = Author.builder()
                .name("FELIX")
                .email(authorRequest.getEmail())
                .biography(authorRequest.getBiography())
                .nationality(authorRequest.getNationality())
                .build();
        when(authorRepository.save(any(Author.class))).thenReturn(authorToSave);

        authorService.addAuthor(authorRequest);

        assertEquals("FELIX", authorToSave.getName());
        verify(authorRepository).save(any(Author.class));
        verify(authorEventPublisher).publishAuthorEvent(any(AuthorEvent.class));
    }

    @DisplayName("Get all author")
    @Test
    void testFindAll(){

        when(authorRepository.findAll()).thenReturn(DataProvider.authorListMock());
        List<AuthorResponse> result = authorService.getAllAuthor();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("Felixon", result.get(0).getName());
        assertEquals("dominicano", result.get(0).getNationality());
        verify(this.authorRepository).findAll();
    }

    @DisplayName("Find author by name")
    @Test
    void testFindByName_whenAuthorExist(){

        when(this.authorRepository.findByName(name)).thenReturn(DataProvider.authorMock());
        AuthorResponse authorFound = this.authorService.findByName(name);

        assertNotNull(authorFound);
        assertEquals(name, authorFound.getName());
        verify(this.authorRepository).findByName(anyString());
    }

    @DisplayName("Test author no found")
    @Test
    void testFindByName_whenAuthorDoesNotExist(){

        when(this.authorRepository.findByName(name)).thenReturn(Optional.empty());
        AuthorResponse authorFound = this.authorService.findByName(name);

        assertNull(authorFound);

        verify(this.authorRepository).findByName(anyString());
    }

    @DisplayName("Test update author")
    @Test
    void testUpdate_whenAuthorExist(){

        Author existingAuthor = new Author();
        existingAuthor.setEmail("flix@gmail.com");

        when(authorRepository.findByName(name)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(any(Author.class))).thenReturn(existingAuthor);

        AuthorResponse updatedAuthor = authorService.updateAuthor(name, authorRequest);

        assertNotNull(updatedAuthor);
        assertEquals(authorRequest.getEmail(), updatedAuthor.getEmail());
        assertEquals(authorRequest.getBiography(), updatedAuthor.getBiography());
        assertEquals(authorRequest.getNationality(), updatedAuthor.getNationality());

        verify(authorRepository).findByName(name);
        verify(authorRepository).save(any(Author.class));

    }

    @DisplayName("Test if the author does not exist")
    @Test
    void testUpdateAuthor_WhenAuthorDoesNotExist(){

        when(authorRepository.findByName(name)).thenReturn(Optional.empty());

        AuthorResponse updatedAuthor = authorService.updateAuthor(name, authorRequest);

        assertNull(updatedAuthor);

        verify(authorRepository).findByName(name);
        verify(authorRepository, never()).save(any(Author.class));
    }

    @DisplayName("Test to delete author")
    @Test
    void testDelete_whenAuthorExist(){

        when(authorRepository.findByName(name)).thenReturn(DataProvider.authorMock());

        AuthorResponse deletedAuthor = authorService.deleteAuthor(name);

        assertNotNull(deletedAuthor);

        verify(authorRepository).findByName(name);
        verify(authorRepository).delete(any(Author.class));

    }

    @DisplayName("Test author not found to delete")
    @Test
    void testDelete_whenAuthorDoesNotExist(){

        when(authorRepository.findByName(name)).thenReturn(Optional.empty());

        AuthorResponse deletedAuthor = authorService.deleteAuthor(name);

        assertNull(deletedAuthor);

        verify(authorRepository).findByName(name);
        verify(authorRepository,never()).delete(any(Author.class));

    }



}
