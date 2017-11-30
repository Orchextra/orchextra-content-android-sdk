package com.gigigo.orchextra.core.domain.entities.article;

import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement;
import java.io.Serializable;

public class ArticleYoutubeVideoElement extends ArticleElement implements Serializable {

  private String source;

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }
}
