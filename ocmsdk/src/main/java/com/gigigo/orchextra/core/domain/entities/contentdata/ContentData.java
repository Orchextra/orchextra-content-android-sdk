package com.gigigo.orchextra.core.domain.entities.contentdata;

import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import java.util.Map;

public class ContentData {

  private ContentItem content;
  private Map<String, ElementCache> elementsCache;
  private String version;
  private String expiredAt;

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

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getExpiredAt() {
    return expiredAt;
  }

  public void setExpiredAt(String expiredAt) {
    this.expiredAt = expiredAt;
  }
}
