package com.example.webecom.controller;

import com.example.webecom.services.ImageStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/image")
public class ImageController {
  @Autowired
  private ImageStorageService imageStorageService;

  @GetMapping(value = "/user/{fileName}")
  public ResponseEntity<byte[]> getImageUser(@PathVariable("fileName") String fileName) {
    byte[] image = imageStorageService.readFileContent(fileName, "user");
    return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.IMAGE_JPEG)
        .body(image);
  }

  @GetMapping(value = "/product/{fileName}")
  public ResponseEntity<byte[]> getImageProduct(@PathVariable("fileName") String fileName) {
    byte[] image = imageStorageService.readFileContent(fileName, "product/images");
    return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.IMAGE_JPEG)
        .body(image);
  }

  @GetMapping(value = "/product/thumbnail/{fileName}")
  public ResponseEntity<byte[]> getImageThumbnailUser(@PathVariable("fileName") String fileName) {
    byte[] image = imageStorageService.readFileContent(fileName, "product/thumbnail");
    return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.IMAGE_JPEG)
        .body(image);
  }

  @GetMapping(value = "/feedback/{fileName}")
  public ResponseEntity<byte[]> getImageFeedback(@PathVariable("fileName") String fileName) {
    byte[] image = imageStorageService.readFileContent(fileName, "feedback/images");
    return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.IMAGE_JPEG)
        .body(image);
  }
}
