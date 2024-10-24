package com.felixon.review_service.integration_test;

import com.felixon.review_service.models.entities.BookReview;
import com.felixon.review_service.repositories.BookReviewRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ReviewIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private BookReviewRepository reviewRepository;

    @Container
    public static MongoDBContainer postgres = new MongoDBContainer("mongo:latest");

    static {
        postgres.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", postgres::getReplicaSetUrl);
    }

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    void shouldReturnAllReviews() {
        reviewRepository.save(new BookReview(1L,"Una vida extraordinaria","Felixon", 5,"Excelente",LocalDate.now()));

        RestAssured.port = port;

        given()
                .when()
                .get("/api/v1/review")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].bookTitle", equalTo("Una vida extraordinaria"));
    }

    @Test
    void shouldFindReviewByTitle(){
        RestAssured.port = port;
        reviewRepository.save(new BookReview(1L,"Una vida extraordinaria","Felixon", 5,"Excelente",LocalDate.now()));

        given()
                .when()
                .get("/api/v1/review/title/Una vida extraordinaria")
                .then()
                .statusCode(200);
    }




    @Test
    void shouldNewReviewSave() {
        RestAssured.port = port;
        given()
                .contentType("application/json")
                .body("""
                             {
                                "bookTitle": "Una vida extraordinaria",
                                "username": "Felixon",
                                "qualification": 5,
                                "comment": "Excelente",
                                "date": "2024-10-10"
                              }
                          """
                )
                .when()
                .post("/api/v1/review/create")
                .then()
                .statusCode(201);
    }

    @Test
    void shouldUpdateUser(){
        RestAssured.port = port;
        reviewRepository.save(new BookReview(1L,"Una vida extraordinaria","Felixon", 5,"Excelente",LocalDate.now()));

        given()
                .contentType("application/json")
                .body( """ 
                            {
                                "qualification": 4,
                                "comment": "muy bueno"
                            }
                          """
                )
                .when()
                .put( "/api/v1/review/update/Felixon")
                .then()
                .statusCode( 201 );
    }

    @Test
    void shouldDeleteBook() {
        RestAssured.port = port;
        reviewRepository.save(new BookReview(1L,"Una vida extraordinaria","Felixon", 5,"Excelente",LocalDate.now()));
        when ()
                .delete("/api/v1/review/delete/Felixon")
                .then()
                .statusCode(200);
    }

}
