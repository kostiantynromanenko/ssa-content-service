package com.kromanenko.ssacontentservice.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StorageHelper {

  public static String getImageObjectName(final String userId, final String imageId, final String format) {
    return "%s/%s.%s".formatted(userId, imageId, format);
  }
}
