package com.gigigo.orchextra.core.domain.entities.elementcache;

import java.util.List;

public class ElementCache {

  private String slug;
  private ElementCacheType type;
  private List<String> tags;
  private ElementCachePreview preview;
  private ElementCacheRender render;
  private ElementCacheShare share;

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public ElementCachePreview getPreview() {
    return preview;
  }

  public void setPreview(ElementCachePreview preview) {
    this.preview = preview;
  }

  public ElementCacheRender getRender() {
    return render;
  }

  public void setRender(ElementCacheRender render) {
    this.render = render;
  }

  public ElementCacheType getType() {
    return type;
  }

  public void setType(ElementCacheType type) {
    this.type = type;
  }

  public ElementCacheShare getShare() {
    return share;
  }

  public void setShare(ElementCacheShare share) {
    this.share = share;
  }
}
