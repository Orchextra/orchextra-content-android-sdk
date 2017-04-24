package com.gigigo.orchextra.core.data.api.dto.article;

public class ApiArticleElementRender {

  private String text;
  private String imageUrl;
  private String elementUrl;
  private String html;
  private String format;
  private String source;
  private String imageThumb;

  // Button
  private String type;
  private String size;
  private String textColor;
  private String bgColor;

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

  public String getType() {
    return type;
  }

  public String getSize() {
    return size;
  }

  public String getTextColor() {
    return textColor;
  }

  public String getBgColor() {
    return bgColor;
  }
}
