package com.gigigo.orchextra.wrapper;

/**
 * Created by alex on 01/12/2017.
 */

public interface OrchextraCompletionCallback {
  void onSuccess();

  void onError(String s);

  void onInit(String s);

  void onConfigurationReceive(String s);
}
