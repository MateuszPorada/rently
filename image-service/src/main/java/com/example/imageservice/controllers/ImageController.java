package com.example.imageservice.controllers;

import com.example.imageservice.dto.ImageRequest;
import com.example.imageservice.services.ImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "api/image")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {

        Optional<byte[]> fileEntityOptional = imageService.findFile(id);

        return fileEntityOptional.map(bytes -> ResponseEntity.ok()
                .contentType(MediaType.valueOf(MediaType.IMAGE_JPEG_VALUE))
                .body(bytes)).orElseGet(() -> ResponseEntity.notFound()
                .build());
    }

    @PostMapping
    public String uploadImage(@RequestBody ImageRequest imageRequest) {

        System.out.println("Image service - uploading image");

        return imageService.uploadImage(imageRequest.getData());
    }

}
