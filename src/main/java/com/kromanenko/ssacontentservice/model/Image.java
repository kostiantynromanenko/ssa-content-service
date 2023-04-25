package com.kromanenko.ssacontentservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "images")
public class Image {

  @Id
  private String id;
  private String userId;
  private String fileName;
  private String format;
}