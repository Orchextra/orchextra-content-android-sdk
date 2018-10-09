package com.gigigo.orchextra.core.sdk.utils;

import com.gigigo.orchextra.core.domain.entities.elements.Element;
import java.util.Comparator;

public class ElementComparator implements Comparator<Element> {

  @Override public int compare(Element a, Element b) {
    return Long.compare(a.getIndex(), b.getIndex());
  }
}
