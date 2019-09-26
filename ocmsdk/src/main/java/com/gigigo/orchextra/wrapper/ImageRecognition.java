package com.gigigo.orchextra.wrapper;


public interface ImageRecognition {

  <T> void setContextProvider(T contextProvider);

  void startImageRecognition(ImageRecognitionCredentials imageRecognitionCredentials);

}
