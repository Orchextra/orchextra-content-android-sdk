package com.gigigo.orchextra.core.domain.utils;

public class GeneratorImageUrl {

  public static String generateImageUrl(String url, int widthPixels, int heightPixels) {
    return url + "/" + widthPixels + "/" + heightPixels;
  }
}
