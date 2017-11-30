package com.gigigo.orchextra.core.domain.entities.article;

import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement;
import java.io.Serializable;
import java.util.List;

public class ArticleImageAndTextElement extends ArticleElement implements Serializable {

  private String text;
  private String imageUrl;
  private List<Float> ratios;

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public List<Float> getRatios() {
    return ratios;
  }

  public void setRatios(List<Float> ratios) {
    this.ratios = ratios;
  }
}
