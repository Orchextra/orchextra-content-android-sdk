package com.gigigo.orchextra.core.sdk.utils;

import com.gigigo.orchextra.ocm.dto.UiMenu;
import java.util.Comparator;

public class MenuListComparator implements Comparator<UiMenu> {

  @Override public int compare(UiMenu a, UiMenu b) {
    return Long.compare(a.getIndex(), b.getIndex());
  }
}
