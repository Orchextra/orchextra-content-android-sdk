package com.gigigo.orchextra.core.sdk.model.grid.dto;

public enum ClipToPadding {
  PADDING_16(16),
  PADDING_14(14),
  PADDING_12(12),
  PADDING_10(10),
  PADDING_8(8),
  PADDING_6(6),
  PADDING_4(4),
  PADDING_2(2),
  PADDING_NONE(0);

  private final int padding;

  ClipToPadding(int padding) {
    this.padding = padding;
  }

  public int getPadding() {
    return padding;
  }
}
