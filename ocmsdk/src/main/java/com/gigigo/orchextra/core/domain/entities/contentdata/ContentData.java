package com.gigigo.orchextra.core.domain.entities.contentdata;

import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import java.util.Map;

public class ContentData {

  private ContentItem content;
  private Map<String, ElementCache> elementsCache;
  private boolean fromCache;

  public ContentItem getContent() {
    return content;
  }

  public void setContent(ContentItem content) {
    this.content = content;
  }

  public Map<String, ElementCache> getElementsCache() {
    return elementsCache;
  }

  public void setElementsCache(Map<String, ElementCache> elementsCache) {
    this.elementsCache = elementsCache;
  }

  public void setFromCache(boolean fromCache) {
    this.fromCache = fromCache;
  }

  public boolean isFromCache() {
    return fromCache;
  }
}
