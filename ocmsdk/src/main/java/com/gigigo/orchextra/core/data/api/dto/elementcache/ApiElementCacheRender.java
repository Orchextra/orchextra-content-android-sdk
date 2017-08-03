package com.gigigo.orchextra.core.data.api.dto.elementcache;


import com.gigigo.orchextra.core.data.api.dto.article.ApiArticleElement;
import java.util.List;

public class ApiElementCacheRender {

  private String contentUrl;
  private String url;

  private String title;
  private List<ApiArticleElement> elements;

  private String schemeUri;

  private String source;
  private String format;
  private boolean federatedAuth;

  public String getContentUrl() {
    return contentUrl;
  }

  public String getUrl() {
    return url;
  }

  public String getTitle() {
    return title;
  }

  public List<ApiArticleElement> getElements() {
    return elements;
  }

  public String getSchemeUri() {
    return schemeUri;
  }

  public String getSource() {
    return source;
  }

  public String getFormat() {
    return format;
  }

  public boolean isFederatedAuth() {
    return federatedAuth;
  }

  public void setFederatedAuth(boolean federatedAuth) {
    this.federatedAuth = federatedAuth;
  }
}
