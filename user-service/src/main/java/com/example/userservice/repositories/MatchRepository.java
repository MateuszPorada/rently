package com.example.userservice.repositories;

import com.example.userservice.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    Optional<Match> findAllByFirstUserIsAndSecondUserIs(String firstUser, String secondUser);

    List<Match> findAllByFirstUser(String firstUser);

    List<Match> findAllBySecondUser(String secondUser);
}
