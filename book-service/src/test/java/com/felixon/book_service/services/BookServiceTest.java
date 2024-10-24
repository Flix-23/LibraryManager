package com.felixon.book_service.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


import com.felixon.book_service.DataProvider;
import com.felixon.book_service.models.dtos.BookEvent;
import com.felixon.book_service.models.dtos.BookRequest;
import com.felixon.book_service.models.dtos.BookResponse;
import com.felixon.book_service.models.entities.Book;
import com.felixon.book_service.repositories.BookRepository;
import com.felixon.book_service.services.consumer.AuthorEventConsumer;
import com.felixon.book_service.services.publisher.BookEventPublisher;
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
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookEventPublisher bookEventPublisher;

    private BookRequest bookRequest;

    private final String title = "la cueva de los indios";

    @BeforeEach
    void setup(){
        bookRequest = new BookRequest();
        bookRequest.setTitle("En busca de la felicidad");
        bookRequest.setGender("Superacion");
        bookRequest.setAvailable(true);
    }

    @DisplayName("Test find all book")
    @Test
    void testFindAllBook(){

        when(bookRepository.findAll()).thenReturn(DataProvider.bookListMock());
        List<BookResponse> bookResponse = bookService.findAllBook();

        assertNotNull(bookResponse);
        assertFalse(bookResponse.isEmpty());
        assertEquals("La cueva de los indios", bookResponse.get(0).getTitle());
        assertEquals("aventura", bookResponse.get(0).getGender());

        verify(this.bookRepository).findAll();

    }

    @DisplayName("Test find author by title")
    @Test
    void testFindByTitle_whenBookExist(){
        String title = "Una vida extraordinaria";
        when(bookRepository.findByTitle(title)).thenReturn(DataProvider.bookMock());
        BookResponse bookResponse = bookService.findBookByTitle(title);

        assertNotNull(bookResponse);
        assertEquals(title, bookResponse.getTitle());
        verify(this.bookRepository).findByTitle(anyString());
    }

    @DisplayName("Test book no found by title")
    @Test
    void testFindByTitle_whenBookDoesNotExist(){
        String title = "Una vida extraordinaria";
        when(bookRepository.findByTitle(title)).thenReturn(Optional.empty());
        BookResponse bookResponse = bookService.findBookByTitle(title);

        assertNull(bookResponse);

        verify(this.bookRepository).findByTitle(anyString());
    }

    @DisplayName("Test find book by gender")
    @Test
    void testFindByGender_whenBookExist(){
        String gender = "Literatura y ficcion";
        when(bookRepository.findByGender(gender)).thenReturn(DataProvider.bookMock());
        BookResponse bookResponse = bookService.findBookByGender(gender);

        assertNotNull(bookResponse);
        assertEquals(gender, bookResponse.getGender());
        verify(this.bookRepository).findByGender(anyString());
    }

    @DisplayName("Test book not found by gender")
    @Test
    void testFindByGender_whenBookDoesNotExist(){
        String gender = "Literatura y ficcion";
        when(bookRepository.findByGender(gender)).thenReturn(Optional.empty());
        BookResponse bookResponse = bookService.findBookByGender(gender);

        assertNull(bookResponse);

        verify(this.bookRepository).findByGender(anyString());
    }

    @DisplayName("Test find book by author name")
    @Test
    void testFindByAuthorName_whenBookExist(){
        String authorName = "Josue";

        when(bookRepository.findByAuthorName(authorName)).thenReturn(DataProvider.bookMock());
        BookResponse bookResponse = bookService.findBookByAuthor(authorName);

        assertNotNull(bookResponse);
        assertEquals(authorName, bookResponse.getAuthorName());
        verify(this.bookRepository).findByAuthorName(anyString());
    }

    @DisplayName("Test book not found by author name")
    @Test
    void testFindByAuthorName_whenBookDoesNotExist(){
        String authorName = "Josue";

        when(bookRepository.findByAuthorName(authorName)).thenReturn(Optional.empty());
        BookResponse bookResponse = bookService.findBookByAuthor(authorName);

        assertNull(bookResponse);

        verify(this.bookRepository).findByAuthorName(anyString());
    }

    @DisplayName("Test to save book")
    @Test
    void testSave_whenBookExist(){
        bookRequest = DataProvider.newBookMock();


        Book bookToSave = Book.builder()
                .title(bookRequest.getTitle())
                .gender(bookRequest.getGender())
                .datePublication(bookRequest.getDatePublication())
                .authorName("Josue")
                .available(bookRequest.isAvailable())
                .build();

        when(bookRepository.save(any(Book.class))).thenReturn(bookToSave);

        bookService.addBook(bookRequest);

        assertEquals("Josue", bookToSave.getAuthorName());
        assertEquals("Literatura y ficcion", bookToSave.getGender());

        verify(bookRepository).save(any(Book.class));
        verify(bookEventPublisher).publishBook(any(BookEvent.class));

    }

    @DisplayName("Test update book")
    @Test
    void testUpdate_whenBookExist(){

        Book existingBook = new Book();

        when(bookRepository.findByTitle(title)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        BookResponse updatedBook = bookService.updateBook(title, bookRequest);

        assertNotNull(updatedBook);
        assertEquals(bookRequest.getTitle(), updatedBook.getTitle());
        assertEquals(bookRequest.getGender(), updatedBook.getGender());
        assertEquals(bookRequest.isAvailable(), updatedBook.isAvailable());

        verify(bookRepository).findByTitle(title);
        verify(bookRepository).save(any(Book.class));

    }

    @DisplayName("Test to delete book")
    @Test
    void testDelete_whenBookExist(){
        when(bookRepository.findByTitle(title)).thenReturn(DataProvider.bookMock());

        BookResponse deletedBook = bookService.deleteBook(title);

        assertNotNull(deletedBook);

        verify(bookRepository).findByTitle(title);
        verify(bookRepository).delete(any(Book.class));
    }

    @DisplayName("Test book not found to delete")
    @Test
    void testDelete_whenBookDoesNotExist(){
        when(bookRepository.findByTitle(title)).thenReturn(Optional.empty());

        BookResponse deletedBook = bookService.deleteBook(title);

        assertNull(deletedBook);

        verify(bookRepository).findByTitle(title);
        verify(bookRepository,never()).delete(any(Book.class));
    }
}
