package com.example.userservice.services;

import com.example.userservice.dto.UserMatchResponse;
import com.example.userservice.entity.Match;
import com.example.userservice.entity.User;
import com.example.userservice.entity.UserDetails;
import com.example.userservice.repositories.MatchRepository;
import com.example.userservice.repositories.UserDetailsRepository;
import com.example.userservice.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final MatchRepository matchRepository;
    private final ImageClient imageClient;

    public MatchService(UserRepository userRepository, UserDetailsRepository userDetailsRepository, MatchRepository matchRepository, ImageClient imageClient) {
        this.userRepository = userRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.matchRepository = matchRepository;
        this.imageClient = imageClient;
    }

    public List<UserMatchResponse> getUserMatchOffers(String userId) {

        UserDetails userDetail = userDetailsRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Set<String> actualMatchesList = findAllUserMatches(userId);

        actualMatchesList.add("123");

        List<UserDetails> userDetails = userDetailsRepository.findAllByAreaIsBetweenAndPriceIsBetweenAndUserIdIsNotIn(
                (int) Math.round(userDetail.getArea() * 0.45),
                (int) Math.round(userDetail.getArea() * 1.55),
                (int) Math.round(userDetail.getPrice() * 0.45),
                (int) Math.round(userDetail.getPrice() * 1.55),
                actualMatchesList
        );

        Set<String> ids = userDetails.stream()
                .map(UserDetails::getUserId)
                .collect(Collectors.toSet());

        List<User> userList = userRepository.findAllByIdIn(ids);

        List<UserMatchResponse> userMatchResponses = createUserListResponse(userList, userDetails, userId);

        Collections.shuffle(userMatchResponses);

        return userMatchResponses;
    }

    public List<UserMatchResponse> getUserMatches(String userId) {

        Set<String> userMatches = findAllUserMatches(userId);

        List<UserDetails> userDetailsList = userDetailsRepository.findAllByUserIdIn(userMatches);

        List<User> userList = userRepository.findAllByIdIn(userMatches);

        return createUserListResponse(userList, userDetailsList, userId);
    }

    public HttpStatus createMatch(String userId, String matchUserId) {

        Optional<User> optionalUser1 = userRepository.findById(userId);
        Optional<User> optionalUser2 = userRepository.findById(matchUserId);

        if (optionalUser1.isEmpty() || optionalUser2.isEmpty()) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (matchForTwoUsersExists(userId, matchUserId)) {

            return HttpStatus.CONFLICT;
        }

        matchRepository.save(new Match(userId, matchUserId));

        return HttpStatus.CREATED;
    }

    public Set<String> findAllUserMatches(String userId) {

        List<Match> matches = matchRepository.findAllByFirstUser(userId);

        matches.addAll(matchRepository.findAllBySecondUser(userId));

        return matches.stream()
                .map(match -> extractSecondUserId(match, userId))
                .collect(Collectors.toSet());
    }

    private List<UserMatchResponse> createUserListResponse(List<User> userList, List<UserDetails> userDetails, String userId) {

        Map<String, UserDetails> userDetailsMap = userDetails.stream()
                .collect(Collectors.toMap(UserDetails::getUserId, Function.identity()));

        return new java.util.ArrayList<>(userList.stream()
                .map(user -> userMatchResponse(user, userDetailsMap.get(user.getId())))
                .filter(user -> !user.getUserId().equals(userId))
                .toList());
    }

    private String extractSecondUserId(Match match, String userId) {

        return match.getSecondUser().equals(userId) ? match.getFirstUser() : match.getSecondUser();
    }

    private boolean matchForTwoUsersExists(String firstUser, String secondUser) {

        Optional<Match> firstCase = matchRepository.findAllByFirstUserIsAndSecondUserIs(firstUser, secondUser);

        Optional<Match> secondCase = matchRepository.findAllByFirstUserIsAndSecondUserIs(secondUser, firstUser);

        return firstCase.isPresent() || secondCase.isPresent();
    }

    private UserMatchResponse userMatchResponse(User user, UserDetails userDetails) {

        return new UserMatchResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                userDetails.getGender(),
                userDetails.getPrice(),
                userDetails.getArea(),
                userDetails.getBio(),
                imageClient.genereateUrl(userDetails.getImage())
        );
    }
}
