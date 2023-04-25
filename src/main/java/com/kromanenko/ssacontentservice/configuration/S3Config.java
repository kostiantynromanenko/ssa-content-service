package com.kromanenko.ssacontentservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

  @Value("${aws.s3.image-bucket}")
  private String imageBucket;
  @Value("${aws.s3.thumbnail-bucket}")
  private String imageThumbnailBucket;
  @Value("${aws.active-profile}")
  private String activeProfile;
  @Value("${aws.region}")
  private String region;

  private final ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create(activeProfile);

  @Bean
  public S3Client s3Client() {
    return S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(credentialsProvider)
        .forcePathStyle(true)
        .build();
  }

  @Bean
  public S3Presigner s3Presigner() {
    return S3Presigner.builder()
        .region(Region.of(region))
        .build();
  }

  public String getImageBucket() {
    return this.imageBucket;
  }

  public String getImageThumbnailBucket() {
    return imageThumbnailBucket;
  }
}
