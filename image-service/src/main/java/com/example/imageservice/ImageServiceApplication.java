package com.example.imageservice;

import com.example.imageservice.entity.Image;
import com.example.imageservice.repositories.ImageRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@SpringBootApplication
public class ImageServiceApplication {

    private final ImageRepository imageRepository;

    public ImageServiceApplication(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }
    @PostConstruct
    public void addDefaultImage() throws IOException {

        Optional<Image> imageOptional = imageRepository.findFirstByDefaultImage(true);

        if (imageOptional.isEmpty()){
            saveDefaultImage();
        }
    }

    private void saveDefaultImage() throws IOException {

        byte[] fileContent = FileUtils.readFileToByteArray(new ClassPathResource("/static/images/avatar.jpg").getFile());

        String encodedImage = Base64.getEncoder().encodeToString(fileContent);

        System.out.println("saving");

        imageRepository.save(new Image(null, encodedImage, true));
    }

    public static void main(String[] args) {
        SpringApplication.run(ImageServiceApplication.class, args);
    }
}
