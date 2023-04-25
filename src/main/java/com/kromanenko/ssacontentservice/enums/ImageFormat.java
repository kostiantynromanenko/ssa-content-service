package com.kromanenko.ssacontentservice.enums;

public enum ImageFormat {

  PNG("png");

  private final String value;

  ImageFormat(final String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
