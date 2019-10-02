package com.gigigo.orchextra.wrapper;


public interface OrchextraCompletionCallback {
  void onSuccess();

  void onError(String s);

  void onInit(String s);

  void onConfigurationReceive(String s);
}
