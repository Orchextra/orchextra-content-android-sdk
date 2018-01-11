package com.gigigo.orchextra.core.domain.entities.elements;

import java.util.List;

public class ElementCustomProperty {
  private String name;
  private String label;
  private String description;
  private String type;
  private String def;
  private List<ElementPropertyOption> options;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDef() {
    return def;
  }

  public void setDef(String def) {
    this.def = def;
  }

  public List<ElementPropertyOption> getOptions() {
    return options;
  }

  public void setOptions(List<ElementPropertyOption> options) {
    this.options = options;
  }
}