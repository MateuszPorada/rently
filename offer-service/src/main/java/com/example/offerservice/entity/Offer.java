package com.example.offerservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@Table(name = "offers")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String ownerId;
    @ElementCollection
    @CollectionTable(name = "offer_images", joinColumns = @JoinColumn(name = "id"))
    private List<String> imageList;
    private String title;
    @Column(columnDefinition = "text")
    private String description;
    private String city;
    private String phoneNumber;
    private int area;
    private int price;
    private Instant date = Instant.now();
}
