package com.gigigo.orchextra.core.domain.entities.article;

import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement;

public class ArticleVideoElement extends ArticleElement {

  private String imageUrl;
  private String format;
  private String source;

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }
}
