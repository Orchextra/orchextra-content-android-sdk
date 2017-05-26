package com.gigigo.orchextra.core.controller.dto;

public class DetailViewInfo {

  private boolean isShareable;
  private String nameArticle;

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
}
