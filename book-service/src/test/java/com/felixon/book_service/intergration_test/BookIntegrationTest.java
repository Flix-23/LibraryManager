package com.felixon.book_service.intergration_test;

import com.felixon.book_service.models.entities.Book;
import com.felixon.book_service.repositories.BookRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class BookIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private BookRepository bookRepository;

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.4");

            static {
                postgres.start();
            }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    void shouldReturnAllBooks() {
        bookRepository.save(new Book(1L,"Una vida extraordinaria","ficcion", LocalDate.now(),"Manuel",true));

        RestAssured.port = port;

        given()
                .when()
                .get("/api/v1/book")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].title", equalTo("Una vida extraordinaria"));
    }

    @Test
    void shouldFindBookByTitle(){
        RestAssured.port = port;

        given()
                .when()
                .get("/api/v1/book/title/Una vida extraordinaria")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldFindBookByGender(){
        RestAssured.port = port;

        given()
                .when()
                .get("/api/v1/book/title/ficcion")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldFindBookByAuthor(){
        RestAssured.port = port;

        given()
                .when()
                .get("/api/v1/book/author/Felixon")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldNewBookSave() {
        RestAssured.port = port;
        given()
                .contentType("application/json")
                .body("""
                             {
                                "title": "Una vida extraordinaria",
                                "gender": "ficcion",
                                "datePublication": "2024-10-10",
                                "authorName": "Mami",
                                "available": true
                              }
                          """
                )
                .when()
                .post("/api/v1/book/create")
                .then()
                .statusCode(201);
    }

    @Test
    void shouldUpdateBook(){
        RestAssured.port = port;
        given()
                .contentType("application/json")
                .body( """ 
                            {
                                "title": "Una vida extraordinaria",
                                "gender": "ficcion",
                                "available": true
                            }
                          """
                        )
                .when()
                .put( "/api/v1/book/update/Una vida extraordinaria")
                .then()
                .statusCode( 201 );
    }

    @Test
    void shouldDeleteBook() {
        RestAssured.port = port;
        when ()
                .delete("/api/v1/book/delete/Una vida extraordinaria")
                .then()
                .statusCode(200);


    }

}
