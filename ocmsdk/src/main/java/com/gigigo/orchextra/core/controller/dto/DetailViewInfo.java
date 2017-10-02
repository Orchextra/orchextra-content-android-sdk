package com.gigigo.orchextra.core.controller.dto;

import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheType;

public class DetailViewInfo {

  private boolean isShareable;
  private String nameArticle;
  private ElementCacheType type;

  public boolean isShareable() {
    return isShareable;
  }

  public void setShareable(boolean shareable) {
    this.isShareable = shareable;
  }

  public String getNameArticle() {
    return nameArticle;
  }

  public void setNameArticle(String nameArticle) {
    this.nameArticle = nameArticle;
  }

  public void setType(ElementCacheType type) {
    this.type = type;
  }

  public ElementCacheType getType() {
    return type;
  }
}
