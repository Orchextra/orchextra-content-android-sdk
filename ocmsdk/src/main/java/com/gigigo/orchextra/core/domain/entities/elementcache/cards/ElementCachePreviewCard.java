package com.gigigo.orchextra.core.domain.entities.elementcache.cards;

import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCachePreview;
import java.util.List;

public class ElementCachePreviewCard {

  private String backgroundColor;
  private String maxColumn;
  private String maxRow;
  private List<ElementCachePreview> previewList;

  public String getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(String backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public String getMaxColumn() {
    return maxColumn;
  }

  public void setMaxColumn(String maxColumn) {
    this.maxColumn = maxColumn;
  }

  public String getMaxRow() {
    return maxRow;
  }

  public void setMaxRow(String maxRow) {
    this.maxRow = maxRow;
  }

  public List<ElementCachePreview> getPreviewList() {
    return previewList;
  }

  public void setPreviewList(List<ElementCachePreview> previewList) {
    this.previewList = previewList;
  }
}
