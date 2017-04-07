package com.gigigo.orchextra.core.data.api.dto.content;

import com.gigigo.orchextra.core.data.api.dto.elements.ApiElement;
import java.util.List;

public class ApiContentItem {

  private String slug;
  private String type;
  private List<String> tags;
  private ApiContentItemLayout layout;
  private List<ApiElement> elements;

  public String getSlug() {
    return slug;
  }

  public String getType() {
    return type;
  }

  public List<String> getTags() {
    return tags;
  }

  public ApiContentItemLayout getLayout() {
    return layout;
  }

  public List<ApiElement> getElements() {
    return elements;
  }
}
