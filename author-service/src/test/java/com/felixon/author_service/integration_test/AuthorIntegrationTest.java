package com.felixon.author_service.integration_test;

import com.felixon.author_service.models.entities.Author;
import com.felixon.author_service.repositories.AuthorRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class AuthorIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private AuthorRepository authorRepository;

    @Container
    public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.33");

    static {
        mySQLContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    void shouldReturnAllAuthors() {
        authorRepository.save(new Author(1L,"Felixon", "felixon@java.com","naci...", "dominicano"));

        RestAssured.port = port;

        given()
                .when()
                .get("/api/v1/author")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].name", equalTo("Felixon"));
    }

    @Test
    void shouldFindBookByName(){
        RestAssured.port = port;
        authorRepository.save(new Author(1L,"Felixon", "felixon@java.com","naci...", "dominicano"));

        given()
                .when()
                .get("/api/v1/author/Felixon")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldSaveBook() {
        RestAssured.port = port;
        given()
                .contentType("application/json")
                .body("""
                             {
                                "name": "Felixon",
                                "email": "felixon@java.com",
                                "biography": "naci...",
                                "nationality": "dominicano"
                              }
                          """
                )
                .when()
                .post("/api/v1/author/create")
                .then()
                .statusCode(201);
    }

    @Test
    void shouldUpdateAuthor(){
        RestAssured.port = port;
        authorRepository.save(new Author(1L,"Felixon", "felixon@java.com","naci...", "dominicano"));

        given()
                .contentType("application/json")
                .body( """ 
                            {
                                "email": "flix@java.com",
                                "biography": "nacion...",
                                "nationality": "dominicano"
                            }
                          """
                )
                .when()
                .put( "/api/v1/author/update/Felixon")
                .then()
                .statusCode( 201 );
    }

    @Test
    void shouldDeleteAuthor() {
        RestAssured.port = port;
        authorRepository.save(new Author(1L,"Felixon", "felixon@java.com","naci...", "dominicano"));

        when ()
                .delete("/api/v1/author/delete/Felixon")
                .then()
                .statusCode(200);


    }

}
