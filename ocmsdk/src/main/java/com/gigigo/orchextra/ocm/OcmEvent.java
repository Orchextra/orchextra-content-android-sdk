package com.gigigo.orchextra.ocm;

/**
 * Created by rui.alonso on 13/1/17.
 */

public enum OcmEvent {
  SHARE("SHARE"),
  CONTENT_START("CONTENT_START"),
  CONTENT_END("CONTENT_END");

  public String event;
  OcmEvent(String event) {
    this.event = event;
  }
}
