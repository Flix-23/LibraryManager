package com.felixon.user_service;

import com.felixon.user_service.models.entities.Role;
import com.felixon.user_service.models.entities.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class DataProvider {
    public static List<User> userListMock(){
        return List.of(new User(1L,"Felixon","1232", LocalDate.now(), true, false, Set.of(new Role(1L, "ROLE_USER"))));
    }

    public static User userMock(){
        return new User(1L,"Felixon","1232", LocalDate.now(), true, false, Set.of(new Role(1L, "ROLE_USER")));
    }

    public static User newUserMock(){
        return new User(null,"Felixon","1234", LocalDate.now(), true, false, Set.of(new Role(1L, "ROLE_USER")));
    }

    public static Role getRoleMock(){
        return new Role(1L, "ROLE_USER");
    }

}
