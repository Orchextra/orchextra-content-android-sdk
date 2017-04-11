package com.gigigo.orchextra.core.data.api.dto.elementcache;

import java.util.List;

public class ApiElementCache {

  private String slug;
  private String type;
  private List<String> tags;
  private ApiElementCachePreview preview;
  private ApiElementCacheRender render;
  private ApiElementCacheShare share;

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
}
