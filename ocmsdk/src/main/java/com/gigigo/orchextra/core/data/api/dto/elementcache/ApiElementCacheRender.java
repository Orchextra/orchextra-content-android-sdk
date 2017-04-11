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
}
