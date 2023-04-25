package com.kromanenko.ssacontentservice.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import com.kromanenko.ssacontentservice.enums.ImageFormat;

public class ImageHelper {

  public static InputStream getImageInputStream(BufferedImage image, ImageFormat formatName) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ImageIO.write(image, formatName.getValue(), outputStream);
    return new ByteArrayInputStream(outputStream.toByteArray());
  }
}
