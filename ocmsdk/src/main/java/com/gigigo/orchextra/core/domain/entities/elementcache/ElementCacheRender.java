package com.gigigo.orchextra.core.domain.entities.elementcache;


import com.gigigo.orchextra.core.domain.entities.article.ArticleElement;
import java.util.List;

public class ElementCacheRender {

  private String contentUrl;
  private String url;

  private String title;
  private List<ArticleElement> elements;

  private String format;
  private String source;

  private String uri;

  public String getContentUrl() {
    return contentUrl;
  }

  public void setContentUrl(String contentUrl) {
    this.contentUrl = contentUrl;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<ArticleElement> getElements() {
    return elements;
  }

  public void setElements(List<ArticleElement> elements) {
    this.elements = elements;
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

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }
}
