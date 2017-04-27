package com.gigigo.orchextra.ocm;

/**
 * Created by rui.alonso on 13/1/17.
 */

public enum OcmEvent {
  SHARE("SHARE"),
  CONTENT_PREVIEW("CONTENT_PREVIEW"),
  CONTENT_FULL("CONTENT_FULL"),
  CELL_CLICKED("CELL_CLICKED"),
  SEARCH("SEARCH"),
  OPEN_BARCODE("OPEN_BARCODE"),
  OPEN_IR("OPEN_IR"),
  VISIT_URL("VISIT_URL"),
  PLAY_YOUTUBE("PLAY_YOUTUBE"),
  VUFORIA("VUFORIA"),
  CONTENT_END("CONTENT_END");

  public String event;
  OcmEvent(String event) {
    this.event = event;
  }
}
