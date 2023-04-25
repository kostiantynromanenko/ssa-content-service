package com.kromanenko.ssacontentservice.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.kromanenko.ssacontentservice.model.Image;
import com.kromanenko.ssacontentservice.repository.ImageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

  private final ImageRepository imageRepository;

  public Image saveImage(final Image image) {
    return imageRepository.save(image);
  }

  public List<Image> getImagesByUserId(final String userId) {
    return imageRepository.getAllByUserId(userId);
  }

  public void deleteImageById(final String imageId) {
    imageRepository.deleteById(imageId);
  }
}
