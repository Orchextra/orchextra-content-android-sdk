package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.vimeo;

public class VimeoInfo {
  private String videoPath;
  private String thumbnailPath;
  private boolean vertical;

  public String getVideoPath() {
    return videoPath;
  }

  public void setVideoPath(String videoPath) {
    this.videoPath = videoPath;
  }

  public String getThumbnailPath() {
    return thumbnailPath;
  }

  public void setThumbnailPath(String thumbnailPath) {
    this.thumbnailPath = thumbnailPath;
  }

  public boolean isVertical() {
    return vertical;
  }

  public void setVertical(boolean vertical) {
    this.vertical = vertical;
  }
}

