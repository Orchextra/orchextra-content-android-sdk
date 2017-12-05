package com.gigigo.orchextra.core.domain.entities.article;

import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement;
import java.io.Serializable;

public class ArticleRichTextElement extends ArticleElement implements Serializable {

  private String html;

  public String getHtml() {
    return html;
  }

  public void setHtml(String html) {
    this.html = html;
  }
}
