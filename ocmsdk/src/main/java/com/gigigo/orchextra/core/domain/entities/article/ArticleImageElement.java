package com.gigigo.orchextra.core.domain.entities.article;

import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement;

public class ArticleImageElement extends ArticleElement {

  private String elementUrl;
  private String imageUrl;
  private String imageThumb;

  public String getElementUrl() {
    return elementUrl;
  }

  public void setElementUrl(String elementUrl) {
    this.elementUrl = elementUrl;
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
