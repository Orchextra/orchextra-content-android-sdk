package com.gigigo.orchextra.ocm.dto;

import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;

public final class UiMenu {
  private int index;
  private String slug;
  private String text;
  private String elementUrl;
  private long updateAt;
  private String contentUrl;
  private ElementCache elementCache;
  private boolean hasNewVersion;

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getElementUrl() {
    return elementUrl;
  }

  public void setElementUrl(String elementUrl) {
    this.elementUrl = elementUrl;
  }

  public long getUpdateAt() {
    return updateAt;
  }

  public void setUpdateAt(long updateAt) {
    this.updateAt = updateAt;
  }

  public void setElementCache(ElementCache elementCache) {
    this.elementCache = elementCache;
  }

  public ElementCache getElementCache() {
    return elementCache;
  }

  public void setContentUrl(String contentUrl) {
    this.contentUrl = contentUrl;
  }

  public String getContentUrl() {
    return contentUrl;
  }

  public boolean hasNewVersion() {
    return hasNewVersion;
  }

  public void setHasNewVersion(boolean hasNewVersion) {
    this.hasNewVersion = hasNewVersion;
  }
}
