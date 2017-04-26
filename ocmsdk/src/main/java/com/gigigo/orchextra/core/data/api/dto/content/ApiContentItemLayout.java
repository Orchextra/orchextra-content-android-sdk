package com.gigigo.orchextra.core.data.api.dto.content;

import java.util.List;

public class ApiContentItemLayout {

  private String name;
  private List<ApiContentItemPattern> pattern;
  private String type;

  public String getName() {
    return name;
  }

  public List<ApiContentItemPattern> getPattern() {
    return pattern;
  }

  public String getType() {
    return type;
  }
}
