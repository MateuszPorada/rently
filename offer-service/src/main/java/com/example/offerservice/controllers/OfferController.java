package com.example.offerservice.controllers;

import com.example.offerservice.dtos.OfferProperty;
import com.example.offerservice.dtos.OfferRequest;
import com.example.offerservice.dtos.OfferResponse;
import com.example.offerservice.services.OfferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/offer")
public class OfferController {
    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping
    public ResponseEntity<OfferResponse> createOffer(@RequestHeader(value = "x-auth-username") String userId, @CookieValue(name = "SESSION") String sessionCookie, @RequestBody OfferRequest offerRequest) {

        return new ResponseEntity<>(offerService.createOffer(userId, sessionCookie, offerRequest), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<OfferResponse> updateOffer(@RequestParam Long offerId, @CookieValue(name = "SESSION") String sessionCookie, @RequestBody OfferRequest offerRequest) {

        return new ResponseEntity<>(offerService.updateOffer(offerId, sessionCookie, offerRequest), HttpStatus.CREATED);
    }

    @DeleteMapping
    public HttpStatus deleteOffer(@RequestParam Long offerId) {

        offerService.deleteOffer(offerId);

        return HttpStatus.OK;
    }

    @GetMapping
    public OfferResponse getOffer(@RequestParam Long id) {

        return offerService.findOffer(id);
    }

    @GetMapping("/list")
    public List<OfferResponse> getOfferList(
            @RequestParam String query,
            @RequestParam int page,
            @RequestParam int elements,
            @RequestParam int minPrice,
            @RequestParam int maxPrice,
            @RequestParam int minArea,
            @RequestParam int maxArea,
            @RequestParam OfferProperty sortBy,
            @RequestParam boolean desc
    ) {

        return offerService.findAllOffers(query, page, elements, minPrice, maxPrice, minArea, maxArea, sortBy, desc);
    }

    @GetMapping("/mylist")
    public List<OfferResponse> getOfferList(@RequestHeader(value = "x-auth-username") String userId) {

        return offerService.findAllOffers(userId);
    }
}
