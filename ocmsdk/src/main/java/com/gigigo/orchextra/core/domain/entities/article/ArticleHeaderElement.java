package com.gigigo.orchextra.core.domain.entities.article;

import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement;
import java.io.Serializable;

public class ArticleHeaderElement extends ArticleElement implements Serializable {

  private String html;
  private String imageUrl;
  private String imageThumb;

  public String getHtml() {
    return html;
  }

  public void setHtml(String html) {
    this.html = html;
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
