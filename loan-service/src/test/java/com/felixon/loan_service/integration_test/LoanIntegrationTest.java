package com.felixon.loan_service.integration_test;

import com.felixon.loan_service.models.entities.BookLoan;
import com.felixon.loan_service.repositories.BookLoanRepository;
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class LoanIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private BookLoanRepository bookLoanRepository;

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
    void shouldReturnAllLoans() {
        bookLoanRepository.save(new BookLoan(1l,"Felixon","Vida", LocalDate.now(), false));
        RestAssured.port = port;

        given()
                .when()
                .get("/api/v1/loan")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].username", equalTo("Felixon"));
    }

    @Test
    void shouldFindLoanById(){
        RestAssured.port = port;
        bookLoanRepository.save(new BookLoan(1l,"Felixon","Vida", LocalDate.now(), false));

        given()
                .when()
                .get("/api/v1/loan/1")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldSaveLoan() {
        RestAssured.port = port;
        given()
                .contentType("application/json")
                .body("""
                             {
                                "username": "Felixon",
                                "bookTitle": "Vida",
                                "date": "2024-10-10",
                                "returned": false
                              }
                          """
                )
                .when()
                .post("/api/v1/loan/create")
                .then()
                .statusCode(201);
    }

    @Test
    void shouldUpdateLoan(){
        RestAssured.port = port;
        bookLoanRepository.save(new BookLoan(1l,"Felixon","Vida", LocalDate.now(), false));

        given()
                .contentType("application/json")
                .body( """ 
                            {
                                "returned": true
                            }
                          """
                )
                .when()
                .put( "/api/v1/loan/update/1")
                .then()
                .statusCode( 201 );
    }
}
