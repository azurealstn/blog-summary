package com.azurealstn.constructorinjection.inject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    @DisplayName("Field Injection 테스트")
    void fieldInjection() {
        UserService userService = new UserService(new UserRepository());
        User user = new User(1L, "userA", 30);
        userService.saveUser(user);
    }
}