package com.example.userservice.controllers;

import com.example.userservice.dto.UserDetailsRequest;
import com.example.userservice.entity.User;
import com.example.userservice.services.UserDetailsService;
import com.example.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/user")
public class UserController {

    private final UserDetailsService userDetailsService;
    private final UserService userService;

    public UserController(UserDetailsService userDetailsService, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @GetMapping("hello")
    public String getHello(@RequestHeader(value = "x-auth-username") String userId) {
        return "Hello " + userId;
    }

    @PostMapping("userdetails")
    public ResponseEntity<Object> createUserDetails(@RequestHeader(value = "x-auth-username") String userId, @CookieValue(name = "SESSION") String sessionCookie, @RequestBody UserDetailsRequest userDetailsRequest) {

        return new ResponseEntity<>(userDetailsService.create(userId, sessionCookie, userDetailsRequest), HttpStatus.CREATED);
    }

    @GetMapping("userdetails")
    public ResponseEntity<Object> getUserDetails(@RequestHeader(value = "x-auth-username") String userId) {

        return new ResponseEntity<>(userDetailsService.getUserDetails(userId), HttpStatus.OK);
    }

    @GetMapping
    public User getUser(@RequestParam String id) {

        return userService.getUser(id);
    }

    @GetMapping("test")
    public String test() {

        return "test";
    }

    @GetMapping("initialized")
    public boolean isUserInitialized(@RequestHeader(value = "x-auth-username") String userId) {

        return userDetailsService.isUserInitialized(userId);
    }

    @GetMapping("list")
    public List<User> getUsers() {

        return userService.getUserList();
    }
}