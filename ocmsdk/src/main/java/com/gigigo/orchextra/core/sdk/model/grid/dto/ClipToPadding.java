package com.gigigo.orchextra.core.sdk.model.grid.dto;

public enum ClipToPadding {
  PADDING_NONE(0),
  PADDING_SMALL(12),
  PADDING_MEDIUM(6),
  PADDING_BIG(2);

  private final int padding;

  ClipToPadding(int padding) {
    this.padding = padding;
  }

  public int getPadding() {
    return padding;
  }
}
