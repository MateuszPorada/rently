package com.example.userservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "matches")
@NoArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstUser;
    private String secondUser;

    public Match(String firstUser, String secondUser) {
        this.firstUser = firstUser;
        this.secondUser = secondUser;
    }
}
