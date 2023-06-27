package com.example.userservice.services;

import com.example.userservice.dto.ImageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ImageClient {

    final String GATEWAY_URI = System.getenv("GATEWAY_URI");
    final String ROOT_URI = "http://" + GATEWAY_URI + "/api/image";
    private final RestTemplate restTemplate;

    public ImageClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String uploadImage(String image, String sessionCookie) {

        HttpHeaders headers = new HttpHeaders();

        headers.add("COOKIE", "SESSION=" + sessionCookie);

        HttpEntity<ImageRequest> entity = new HttpEntity<>(new ImageRequest(image), headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(ROOT_URI, entity, String.class);

        return responseEntity.getBody();
    }

    public String genereateUrl(String imageId) {

        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(GATEWAY_URI)
                .path("/api/image/")
                .path(imageId)
                .build().toString();
    }
}
