package com.engine.controller;

import com.engine.model.UserEntity;
import com.engine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/register")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public UserEntity createUser(@RequestBody @Valid UserEntity userEntity) {
        return userService.registerNewUser(userEntity);
    }
}
