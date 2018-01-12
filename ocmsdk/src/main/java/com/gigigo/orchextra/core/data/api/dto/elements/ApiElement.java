package com.gigigo.orchextra.core.data.api.dto.elements;

import java.util.List;

public class ApiElement {

  private List<String> tags;
  private ApiElementSegmentation segmentation;
  private ApiElementCustomProperty customProperties;
  private ApiElementSectionView sectionView;
  private String slug;
  private String elementUrl;
  private String name;
  private List<List<String>> dates;

  public ApiElementSegmentation getSegmentation() {
    return segmentation;
  }

  public ApiElementCustomProperty getCustomProperties() {
    return customProperties;
  }

  public ApiElementSectionView getSectionView() {
    return sectionView;
  }

  public String getSlug() {
    return slug;
  }

  public String getElementUrl() {
    return elementUrl;
  }

  public List<String> getTags() {
    return tags;
  }

  public String getName() {
    return name;
  }

  public List<List<String>> getDates() {
    return dates;
  }
}
