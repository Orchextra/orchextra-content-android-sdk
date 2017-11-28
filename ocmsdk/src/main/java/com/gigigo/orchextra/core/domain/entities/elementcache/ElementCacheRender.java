package com.gigigo.orchextra.core.domain.entities.elementcache;


import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement;
import java.io.Serializable;
import java.util.List;

public class ElementCacheRender implements Serializable {

  private String contentUrl;
  private String url;

  private String title;
  private List<ArticleElement> elements;

  private VideoFormat format;
  private String source;

  private String schemeUri;

  private FederatedAuthorization federatedAuth;

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

  public VideoFormat getFormat() {
    return format;
  }

  public void setFormat(VideoFormat format) {
    this.format = format;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getUri() {
    return schemeUri;
  }

  public void setSchemeUri(String schemeUri) {
    this.schemeUri = schemeUri;
  }

  public FederatedAuthorization getFederatedAuth() {
    return federatedAuth;
  }

  public void setFederatedAuth(FederatedAuthorization federatedAuth) {
    this.federatedAuth = federatedAuth;
  }
}
