package com.gigigo.orchextra.core.data.api.dto.article;

import java.util.List;

public class ApiArticleElementRender {

  private String text;
  private String imageUrl;
  private String elementUrl;
  private String html;
  private String format;
  private String source;
  private String imageThumb;
  private List<Float> ratios;

  public String getText() {
    return text;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getElementUrl() {
    return elementUrl;
  }

  public String getHtml() {
    return html;
  }

  public String getFormat() {
    return format;
  }

  public String getSource() {
    return source;
  }

  public String getImageThumb() {
    return imageThumb;
  }

  public List<Float> getRatios() {
    return ratios;
  }
}
