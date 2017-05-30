package com.gigigo.orchextra.core.data.api.dto.elements;

import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCache;
import com.mskn73.kache.Kacheable;
import org.jetbrains.annotations.NotNull;

public class ApiElementData implements Kacheable {
  private ApiElementCache element;

  public ApiElementCache getElement() {
    return element;
  }

  @NotNull @Override public String getKey() {
    return element.getSlug();
  }
}
