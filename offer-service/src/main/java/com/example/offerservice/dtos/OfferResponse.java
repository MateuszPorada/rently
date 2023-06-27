package com.example.offerservice.dtos;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class OfferResponse {

    private Long id;
    private String title;
    private String description;
    private String city;
    private String phoneNumber;
    private int area;
    private int price;
    private List<String> imageList;
    private Instant date;
}
