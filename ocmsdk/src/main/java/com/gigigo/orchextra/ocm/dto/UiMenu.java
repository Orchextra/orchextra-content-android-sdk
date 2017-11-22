package com.gigigo.orchextra.ocm.dto;

public final class UiMenu {
  private String slug;
  private String text;
  private String elementUrl;
  private long updateAt;
  private String contentUrl;

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

  public void setContentUrl(String contentUrl) {
    this.contentUrl = contentUrl;
  }

  public String getContentUrl() {
    return contentUrl;
  }
}
