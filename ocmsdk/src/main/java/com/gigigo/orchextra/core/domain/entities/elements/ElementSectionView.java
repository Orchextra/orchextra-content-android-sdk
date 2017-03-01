package com.gigigo.orchextra.core.domain.entities.elements;

public class ElementSectionView {

  private String text;
  private String imageUrl;
  private String imageThumb;

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getImageThumb() {
    return imageThumb;
  }

  public void setImageThumb(String imageThumb) {
    this.imageThumb = imageThumb;
  }
}
