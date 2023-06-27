package com.example.userservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "user_details")
@NoArgsConstructor
public class UserDetails {
    @Id
    @Column(name = "user_id")
    private String userId;
    @Column(columnDefinition = "text")
    private String bio;
    private Gender gender;
    private int price;
    private int area;
    @Column(columnDefinition = "text")
    private String image = "default";
}
