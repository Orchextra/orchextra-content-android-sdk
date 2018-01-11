package com.gigigo.orchextra.core.data.api.dto.elements;

import java.util.List;

public class ApiElementCustomProperty {
  private String name;
  private String label;
  private String description;
  private String type;
  private String def;
  private List<ApiElementPropertyOption> options;

  public String getName() {
    return name;
  }

  public String getLabel() {
    return label;
  }

  public String getDescription() {
    return description;
  }

  public String getType() {
    return type;
  }

  public String getDef() {
    return def;
  }

  public List<ApiElementPropertyOption> getOptions() {
    return options;
  }
}
