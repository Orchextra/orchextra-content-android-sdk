package com.gigigo.orchextra.core.data.api.dto.content;

import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCache;
import com.mskn73.kache.Kacheable;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class ApiSectionContentData implements Kacheable {

  private ApiContentItem content;
  private Map<String, ApiElementCache> elementsCache;
  private String key;

  public ApiContentItem getContent() {
    return content;
  }

  public Map<String, ApiElementCache> getElementsCache() {
    return elementsCache;
  }

  public void setKey(String key) {
    this.key = key;
  }

  @NotNull @Override public String getKey() {
    return key;
  }
}
