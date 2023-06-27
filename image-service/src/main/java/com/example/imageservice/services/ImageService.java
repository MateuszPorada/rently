package com.example.imageservice.services;

import com.example.imageservice.entity.Image;
import com.example.imageservice.repositories.ImageRepository;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Optional<byte[]> findFile(String id) {

        Optional<Image> optionalImage = id.equals("default") ? imageRepository.findFirstByDefaultImage(true) : imageRepository.findById(id);

        return optionalImage.map(image -> Base64.getDecoder().decode(image.getData()));
    }

    public String uploadImage(String imageData) {

        Image image = new Image();

        image.setData(imageData);

        return imageRepository.save(image).getId();
    }
}
