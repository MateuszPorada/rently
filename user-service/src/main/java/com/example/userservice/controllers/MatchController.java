package com.example.userservice.controllers;

import com.example.userservice.services.MatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/user/match")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("offer")
    public ResponseEntity<Object> getMatchOffers(@RequestHeader(value = "x-auth-username") String userId) {

        return new ResponseEntity<>(matchService.getUserMatchOffers(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addMatch(@RequestHeader(value = "x-auth-username") String userId, @RequestParam String matchUserId) {

        return new ResponseEntity<>(matchService.createMatch(userId, matchUserId));
    }

    @GetMapping
    public ResponseEntity<Object> getUserMatches(@RequestHeader(value = "x-auth-username") String userId) {

        return new ResponseEntity<>(matchService.getUserMatches(userId), HttpStatus.OK);
    }
}