package com.gigigo.orchextra.core.domain.entities.article;

import com.gigigo.orchextra.core.domain.entities.article.base.ArticleButtonSize;
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleButtonType;
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement;
import java.io.Serializable;

public class ArticleButtonElement extends ArticleElement implements Serializable {

  private ArticleButtonType type;
  private ArticleButtonSize size;
  private String elementUrl;
  private String text;
  private String textColor;
  private String bgColor;
  private String imageUrl;

  public ArticleButtonType getType() {
    return type;
  }

  public void setType(ArticleButtonType type) {
    this.type = type;
  }

  public ArticleButtonSize getSize() {
    return size;
  }

  public void setSize(ArticleButtonSize size) {
    this.size = size;
  }

  public String getElementUrl() {
    return elementUrl;
  }

  public void setElementUrl(String elementUrl) {
    this.elementUrl = elementUrl;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getTextColor() {
    return textColor;
  }

  public void setTextColor(String textColor) {
    this.textColor = textColor;
  }

  public String getBgColor() {
    return bgColor;
  }

  public void setBgColor(String bgColor) {
    this.bgColor = bgColor;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}
