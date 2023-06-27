package com.example.userservice.dto;

import com.example.userservice.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserMatchResponse {

    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private Gender gender;
    private int price;
    private int area;
    private String bio;
    private String image;
}
