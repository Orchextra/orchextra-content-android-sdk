package com.gigigo.orchextra.core.sdk.model.grid.dto;

public enum ClipToPadding {
  PADDING_16(16),
  PADDING_15(15),
  PADDING_14(14),
  PADDING_13(13),
  PADDING_12(12),
  PADDING_11(11),
  PADDING_10(10),
  PADDING_9(9),
  PADDING_8(8),
  PADDING_7(7),
  PADDING_6(6),
  PADDING_5(5),
  PADDING_4(4),
  PADDING_3(3),
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
