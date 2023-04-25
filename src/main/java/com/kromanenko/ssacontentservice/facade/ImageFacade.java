package com.kromanenko.ssacontentservice.facade;

import static com.kromanenko.ssacontentservice.enums.ImageFormat.PNG;
import static com.kromanenko.ssacontentservice.utils.StorageHelper.getImageObjectName;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.List;
import java.util.function.Function;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.kromanenko.ssacontentservice.configuration.S3Config;
import com.kromanenko.ssacontentservice.dto.ImageDto;
import com.kromanenko.ssacontentservice.exception.ImageProcessingException;
import com.kromanenko.ssacontentservice.model.Image;
import com.kromanenko.ssacontentservice.service.ImageProcessingService;
import com.kromanenko.ssacontentservice.service.ImageService;
import com.kromanenko.ssacontentservice.service.S3Service;
import com.kromanenko.ssacontentservice.utils.ImageHelper;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageFacade {

  private final S3Service s3Service;
  private final ImageService imageService;
  private final ImageProcessingService imageProcessingService;
  private final S3Config s3Config;

  public void saveImage(final MultipartFile imageFile, final String userId) throws ImageProcessingException {
    String imageId = null;
    String imageObjectName = null;
    String thumbnailObjectName = null;

    try {
      var image = createImage(userId, imageFile.getOriginalFilename());
      var dbImage = imageService.saveImage(image);

      imageId = dbImage.getId();
      imageObjectName = getImageObjectName(userId, imageId, image.getFormat());

      s3Service.putObject(s3Config.getImageBucket(), imageObjectName, imageFile.getInputStream());

      var thumbnail = imageProcessingService.createThumbnail(imageFile.getInputStream(), imageId);
      thumbnailObjectName = getImageObjectName(userId, imageId, PNG.getValue());

      s3Service.putObject(s3Config.getImageThumbnailBucket(), thumbnailObjectName,
          ImageHelper.getImageInputStream(thumbnail, PNG)
      );

      log.info("Image (key = {}) has been stored ", imageObjectName);
    } catch (Exception e) {
      try {
        if (isNotBlank(userId)) {
          imageService.deleteImageById(imageId);
        }
        if (isNotBlank(imageObjectName)) {
          s3Service.deleteObject(s3Config.getImageBucket(), imageObjectName);
        }
        if (isNotBlank(thumbnailObjectName)) {
          s3Service.deleteObject(s3Config.getImageThumbnailBucket(), thumbnailObjectName);
        }
      } catch (Exception ex) {
        log.error("Cannot rollback image data after error", ex);
        throw new ImageProcessingException(ex);
      }

      throw new ImageProcessingException(e);
    }
  }

  public List<ImageDto> getImages(final String userId) {
    return imageService.getImagesByUserId(userId).stream()
        .map(buildImage(userId))
        .toList();
  }

  private Function<Image, ImageDto> buildImage(String userId) {
    return image -> {
      var dto = new ImageDto();
      dto.setId(image.getId());
      dto.setName(image.getFileName());

      var thumbnailObjectName = getImageObjectName(userId, image.getId(), PNG.getValue());
      var thumbnailUrl = s3Service.generatePreSignedUrlForObject(s3Config.getImageThumbnailBucket(),
          thumbnailObjectName);

      dto.setThumbnailUrl(thumbnailUrl.toString());

      return dto;
    };
  }

  private Image createImage(final String userId, final String fileName) {
    var image = new Image();
    image.setUserId(userId);
    image.setFileName(fileName);
    image.setFormat(FilenameUtils.getExtension(fileName));

    return image;
  }
}
