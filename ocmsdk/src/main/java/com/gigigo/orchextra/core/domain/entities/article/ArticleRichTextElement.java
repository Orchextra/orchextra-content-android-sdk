package com.gigigo.orchextra.core.domain.entities.article;

import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement;

public class ArticleRichTextElement extends ArticleElement {

  private String html;

  public String getHtml() {
    return html;
  }

  public void setHtml(String html) {
    this.html = html;
  }
}
