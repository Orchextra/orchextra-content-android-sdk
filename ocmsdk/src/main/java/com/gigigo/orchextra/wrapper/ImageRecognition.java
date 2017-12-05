package com.gigigo.orchextra.wrapper;

/**
 * Created by alex on 01/12/2017.
 */

public interface ImageRecognition {

  <T> void setContextProvider(T contextProvider);

  void startImageRecognition(ImageRecognitionCredentials imageRecognitionCredentials);

}
