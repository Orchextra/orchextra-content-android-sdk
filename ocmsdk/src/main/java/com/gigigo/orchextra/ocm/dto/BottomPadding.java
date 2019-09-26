package com.gigigo.orchextra.ocm.dto;


public enum BottomPadding {
  PADDING_40(1),
  PADDING_35(2),
  PADDING_30(3),
  PADDING_25(4),
  PADDING_20(5),
  PADDING_15(6),
  PADDING_12(7),
  PADDING_10(8),
  PADDING_8(9),
  PADDING_6(10),
  PADDING_4(15),
  PADDING_2(20),
  PADDING_1(25);

  private final int size;

  BottomPadding(int size) {
    this.size = size;
  }

  public int getSize() {
    return size;
  }
}
