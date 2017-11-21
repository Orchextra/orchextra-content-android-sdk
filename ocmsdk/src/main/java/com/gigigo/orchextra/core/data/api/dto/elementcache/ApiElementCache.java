package com.gigigo.orchextra.core.data.api.dto.elementcache;

import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementSegmentation;
import java.util.List;

public class ApiElementCache {

  private String elementUrl;
  private String slug;
  private String type;
  private List<String> tags;
  private ApiElementCachePreview preview;
  private ApiElementCacheRender render;
  private ApiElementCacheShare share;
  private ApiElementSegmentation segmentation;
  private String name;
  private long updatedAt;

  public String getElementUrl() {
    return elementUrl;
  }

  public String getSlug() {
    return slug;
  }

  public String getType() {
    return type;
  }

  public List<String> getTags() {
    return tags;
  }

  public ApiElementCachePreview getPreview() {
    return preview;
  }

  public ApiElementCacheRender getRender() {
    return render;
  }

  public ApiElementCacheShare getShare() {
    return share;
  }

  public ApiElementSegmentation getSegmentation() {
    return segmentation;
  }

  public String getName() {
    return name;
  }

  public long getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(long updatedAt) {
    this.updatedAt = updatedAt;
  }
}
