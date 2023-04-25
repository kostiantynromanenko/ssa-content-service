package com.kromanenko.ssacontentservice.service;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import net.coobird.thumbnailator.Thumbnails;

import com.kromanenko.ssacontentservice.exception.ThumbnailException;

@Service
public class ImageProcessingService {

  public BufferedImage createThumbnail(final InputStream imageInputStream, final String originalImageName) {
    try {
      var originalImage = ImageIO.read(imageInputStream);

      return Thumbnails.of(originalImage)
          .scale(getScaleFactor(originalImage.getWidth(), originalImage.getHeight()))
          .asBufferedImage();
    } catch (Exception e) {
      throw new ThumbnailException("Cannot create thumbnail for image (id = %s)".formatted(originalImageName), e);
    }
  }

  // TODO improve algorythm
  private double getScaleFactor(final int width, final int height) {
    return width > 900 || height > 900 ? 0.5 : 1;
  }
}
