package com.gigigo.orchextra.core.domain.entities.elementcache;

import com.gigigo.orchextra.core.domain.entities.elements.ElementSegmentation;
import java.util.List;

public class ElementCache {

  private String slug;
  private ElementCacheType type;
  private List<String> tags;
  private ElementCachePreview preview;
  private ElementCacheRender render;
  private ElementCacheShare share;
  private ElementSegmentation segmentation;
  private String name;
  private long updateAt;

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

  public ElementSegmentation getSegmentation() {
    return segmentation;
  }

  public void setSegmentation(ElementSegmentation segmentation) {
    this.segmentation = segmentation;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setUpdateAt(long updateAt) {
    this.updateAt = updateAt;
  }

  public long getUpdateAt() {
    return updateAt;
  }
}
