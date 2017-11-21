package com.gigigo.orchextra.core.data.api.dto.elements;

import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCache;
import com.mskn73.kache.Kacheable;
import org.jetbrains.annotations.NotNull;

//@KacheLife(expiresTime = 1000 * 60 * 60 * 24) // 1 day
public class ApiElementData implements Kacheable {
  private ApiElementCache element;

  public ApiElementData() {
  }

  public ApiElementData(ApiElementCache element) {
    this.element = element;
  }

  public ApiElementCache getElement() {
    return element;
  }

  @NotNull @Override public String getKey() {
    return element.getElementUrl();
  }
}
