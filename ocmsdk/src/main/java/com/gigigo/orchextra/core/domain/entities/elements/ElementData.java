package com.gigigo.orchextra.core.domain.entities.elements;

import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;

public class ElementData {

  private ElementCache element;

  public void setElement(ElementCache element) {
    this.element = element;
  }

  public ElementCache getElement() {
    return element;
  }
}
