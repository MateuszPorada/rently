package com.example.userservice.services;

import com.example.userservice.dto.UserDetailsRequest;
import com.example.userservice.dto.UserDetailsResponse;
import com.example.userservice.entity.User;
import com.example.userservice.entity.UserDetails;
import com.example.userservice.repositories.UserDetailsRepository;
import com.example.userservice.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@Service
public class UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ImageClient imageClient;

    public UserDetailsService(UserDetailsRepository userDetailsRepository, UserRepository userRepository, ModelMapper modelMapper, ImageClient imageClient) {
        this.userDetailsRepository = userDetailsRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.imageClient = imageClient;
    }

    @Transactional
    public UserDetailsResponse create(String userId, String sessionCookie, UserDetailsRequest userDetailsRequest) {

        UserDetails userDetails = modelMapper.map(userDetailsRequest, UserDetails.class);

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        userDetails.setUserId(userId);

        if (userDetailsRequest.getImage() != null) {
            System.out.println(userDetailsRequest.getImage());
            userDetails.setImage(imageClient.uploadImage(userDetailsRequest.getImage(), sessionCookie));
        }else {

            userDetails.setImage("default");
        }

        UserDetailsResponse userDetailsResponse = modelMapper.map(userDetailsRepository.save(userDetails), UserDetailsResponse.class);

        user.setFirstName(userDetailsRequest.getFirstName());
        user.setLastName(userDetailsRequest.getLastName());
        user.setEmailVerified(true);

        User savedUser = userRepository.save(user);

        userDetailsResponse.setImage(imageClient.genereateUrl(userDetailsResponse.getImage()));
        userDetailsResponse.setFirstName(savedUser.getFirstName());
        userDetailsResponse.setLastName(savedUser.getLastName());
        userDetailsResponse.setEmail(savedUser.getEmail());

        return userDetailsResponse;
    }

    public boolean isUserInitialized(String userId) {

        return userDetailsRepository.existsById(userId);
    }

    public UserDetailsResponse getUserDetails(String userId) {

        UserDetails userDetails = userDetailsRepository
                .findByUserId(userId)
                .orElseGet(UserDetails::new);

        UserDetailsResponse userDetailsResponse = modelMapper.map(userDetails, UserDetailsResponse.class);

        userDetailsResponse.setImage(imageClient.genereateUrl(userDetails.getImage()));

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        userDetailsResponse.setUserId(user.getId());
        userDetailsResponse.setFirstName(user.getFirstName());
        userDetailsResponse.setLastName(user.getLastName());
        userDetailsResponse.setEmail(user.getEmail());

        return userDetailsResponse;
    }
}
