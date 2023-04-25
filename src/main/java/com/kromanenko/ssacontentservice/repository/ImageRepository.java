package com.kromanenko.ssacontentservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kromanenko.ssacontentservice.model.Image;

@Repository
public interface ImageRepository extends MongoRepository<Image, String> {

  List<Image> getAllByUserId(String userId);
}
