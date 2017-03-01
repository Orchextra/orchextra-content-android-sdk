package com.gigigo.orchextra.core.domain.entities.elementcache;

public class ElementCachePreview {

  private String imageUrl;
  private String text;
  private ElementCacheBehaviour behaviour;
  private String imageThumb;

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public ElementCacheBehaviour getBehaviour() {
    return behaviour;
  }

  public void setBehaviour(ElementCacheBehaviour behaviour) {
    this.behaviour = behaviour;
  }

  public String getImageThumb() {
    return imageThumb;
  }

  public void setImageThumb(String imageThumb) {
    this.imageThumb = imageThumb;
  }
}
