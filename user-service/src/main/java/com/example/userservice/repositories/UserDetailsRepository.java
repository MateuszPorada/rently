package com.example.userservice.repositories;

import com.example.userservice.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, String> {

    List<UserDetails> findAllByAreaIsBetweenAndPriceIsBetweenAndUserIdIsNotIn(int area, int area2, int price, int price2, Set<String> userId);

    List<UserDetails> findAllByUserIdIn(Set<String> userIdList);

    Optional<UserDetails> findByUserId(String userId);
}