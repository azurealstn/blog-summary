package com.azurealstn.constructorinjection.inject;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public User selectUser() {
        User user = new User(1L, "userA", 30);
        userService.saveUser(user); //유저 생성
        return userService.selectUser(user.getId()); //유저 조회
    }
}
