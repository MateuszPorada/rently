package com.example.offerservice.repositories;

import com.example.offerservice.entity.Offer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends PagingAndSortingRepository<Offer, Long> {

    List<Offer> findAllByOwnerId(String ownerId);

    List<Offer> findAllByTitleContainingIgnoreCaseAndPriceBetweenAndAreaBetween(String ownerId, int priceMin, int priceMax, int areaMin, int areaMax, Pageable pageable);
}
