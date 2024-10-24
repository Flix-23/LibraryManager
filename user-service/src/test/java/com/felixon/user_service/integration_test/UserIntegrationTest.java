package com.felixon.user_service.integration_test;

import com.felixon.user_service.models.entities.User;
import com.felixon.user_service.repositories.UserRepository;
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
import java.util.HashSet;


import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class UserIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

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
    void shouldReturnAllUsers() {
        userRepository.save(new User(1l,"Felixon","1234", LocalDate.now() ,true,false, new HashSet<>()));
        RestAssured.port = port;

        given()
                .when()
                .get("/api/v1/user")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].username", equalTo("Felixon"));
    }

    @Test
    void shouldFindUserByName(){
        RestAssured.port = port;
        userRepository.save(new User(1l,"Felixon","123443546", LocalDate.now() ,true,false, new HashSet<>()));
        given()
                .when()
                .get("/api/v1/user/Felixon")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldSaveUser() {
        RestAssured.port = port;
        given()
                .contentType("application/json")
                .body("""
                             {
                                "username": "Felixon",
                                "password": "123409865"
                              }
                          """
                )
                .when()
                .post("/api/v1/user/register")
                .then()
                .statusCode(201);
    }

    @Test
    void shouldUpdateUser(){
        RestAssured.port = port;
        userRepository.save(new User(1L,"Josue","123423323", LocalDate.now() ,true, false, new HashSet<>()));
        given()
                .contentType("application/json")
                .body( """ 
                            {
                                "username": "Josue",
                                "password": "12342432",
                                "author": false
                            
                            }
                          """
                )
                .when()
                .put( "/api/v1/user/update/Josue")
                .then()
                .statusCode( 201 );
    }


}
