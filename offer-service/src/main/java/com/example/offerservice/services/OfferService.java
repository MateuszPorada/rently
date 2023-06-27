package com.example.offerservice.services;

import com.example.offerservice.dtos.OfferProperty;
import com.example.offerservice.dtos.OfferRequest;
import com.example.offerservice.dtos.OfferResponse;
import com.example.offerservice.entity.Offer;
import com.example.offerservice.repositories.OfferRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;

    private final ImageClient imageClient;

    public OfferService(OfferRepository offerRepository, ModelMapper modelMapper, ImageClient imageClient) {
        this.offerRepository = offerRepository;
        this.modelMapper = modelMapper;
        this.imageClient = imageClient;
    }

    public List<OfferResponse> findAllOffers(String query, int page, int elements, int minPrice, int maxPrice, int minArea, int maxArea, OfferProperty sortBy, boolean desc) {

        Sort sort = desc ? Sort.by(sortBy.getProperty()).descending() : Sort.by(sortBy.getProperty()).ascending();

        Pageable pageable = PageRequest.of(page, elements, sort);

        return offerRepository.findAllByTitleContainingIgnoreCaseAndPriceBetweenAndAreaBetween(query, minPrice, maxPrice, minArea, maxArea, pageable)
                .stream()
                .map(this::createResponseDto)
                .collect(Collectors.toList());
    }

    public List<OfferResponse> findAllOffers(String userId) {

        return offerRepository.findAllByOwnerId(userId)
                .stream()
                .map(this::createResponseDto)
                .collect(Collectors.toList());
    }

    public OfferResponse createOffer(String userId, String sessionCookie, OfferRequest offerRequest) {

        if (offerRequest.getImageList().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empty image list");
        }

        List<String> imageIds = offerRequest.getImageList().stream()
                .map(image -> imageClient.uploadImage(image, sessionCookie))
                .toList();

        offerRequest.setImageList(imageIds);

        Offer offer = modelMapper.map(offerRequest, Offer.class);

        offer.setOwnerId(userId);

        return createResponseDto(offerRepository.save(offer));
    }

    public OfferResponse updateOffer(Long offerId, String sessionCookie, OfferRequest offerRequest) {

        if (offerRequest.getImageList().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empty image list");
        }

        List<String> imageIds = offerRequest.getImageList().stream()
                .map(image -> imageClient.uploadImage(image, sessionCookie))
                .toList();

        offerRequest.setImageList(imageIds);

        Offer oldOffer = offerRepository
                .findById(offerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Offer not found"));

        Offer offer = modelMapper.map(offerRequest, Offer.class);

        offer.setOwnerId(oldOffer.getOwnerId());
        offer.setId(oldOffer.getId());
        offer.setDate(oldOffer.getDate());

        return createResponseDto(offerRepository.save(offer));
    }

    public OfferResponse findOffer(Long id) {

        Optional<Offer> optionalOffer = offerRepository.findById(id);

        return optionalOffer.map(this::createResponseDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private OfferResponse createResponseDto(Offer offer) {

        List<String> imageIds = offer.getImageList().stream()
                .map(imageClient::genereateUrl)
                .toList();

        offer.setImageList(imageIds);

        return modelMapper.map(offer, OfferResponse.class);
    }

    public void deleteOffer(Long offerId) {

        offerRepository.findById(offerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Offer with given id was not found"));

        offerRepository.deleteById(offerId);
    }
}
