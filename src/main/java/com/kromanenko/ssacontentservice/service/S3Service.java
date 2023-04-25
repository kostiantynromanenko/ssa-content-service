package com.kromanenko.ssacontentservice.service;

import java.io.InputStream;
import java.net.URL;
import java.time.Duration;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import lombok.RequiredArgsConstructor;

import com.kromanenko.ssacontentservice.exception.StorageException;

@Service
@RequiredArgsConstructor
public class S3Service {

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;

  public void putObject(
      final String bucket,
      final String key,
      final InputStream inputStream
  ) throws StorageException {
    var putObjectRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();

    try {
      s3Client.putObject(putObjectRequest,
          RequestBody.fromInputStream(inputStream, inputStream.available()));
    } catch (Exception e) {
      throw new StorageException("Cannot create S3 object (key = %s)".formatted(key), e);
    }
  }

  public void deleteObject(final String bucket, final String key) throws StorageException {
    var deleteObjectRequest = DeleteObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();

    try {
      s3Client.deleteObject(deleteObjectRequest);
    } catch (Exception e) {
      throw new StorageException("Cannot delete S3 object (key = %s)".formatted(key), e);
    }
  }

  public URL generatePreSignedUrlForObject(final String bucket, final String objectKey) {
    var getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(objectKey)
        .build();

    GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
        .getObjectRequest(getObjectRequest)
        .signatureDuration(Duration.ofDays(1))
        .build();

    try {
      return s3Presigner.presignGetObject(getObjectPresignRequest).url();
    } catch (Exception e) {
      throw new StorageException("Cannot generate S3 pre-signed url for object (key = %s)".formatted(objectKey), e);
    }
  }
}
