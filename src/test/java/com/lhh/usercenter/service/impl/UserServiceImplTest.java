package com.lhh.usercenter.service.impl;

import com.lhh.usercenter.model.domain.User;
import com.lhh.usercenter.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;


    @Test
    void userRegister() {
        long result = userService.userRegister("kuuuure", "12345678", "12345678");
        System.out.println(result);
    }
}