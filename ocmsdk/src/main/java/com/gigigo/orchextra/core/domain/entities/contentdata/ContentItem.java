package com.gigigo.orchextra.core.domain.entities.contentdata;

import com.gigigo.orchextra.core.domain.entities.elements.Element;
import java.util.List;

public class ContentItem {

  private String slug;
  private String type;
  private List<String> tags;
  private ContentItemLayout layout;
  private List<Element> elements;

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public ContentItemLayout getLayout() {
    return layout;
  }

  public void setLayout(ContentItemLayout layout) {
    this.layout = layout;
  }

  public List<Element> getElements() {
    return elements;
  }

  public void setElements(List<Element> elements) {
    this.elements = elements;
  }
}
