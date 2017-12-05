package com.gigigo.orchextra.core.domain.entities.elementcache;

import java.io.Serializable;

public enum VideoFormat implements Serializable {
  YOUTUBE("youtube"),
  VIMEO("vimeo"),
  NONE("");

  private final String videoFormat;

  VideoFormat(String videoFormat) {
    this.videoFormat = videoFormat;
  }

  public static VideoFormat convertStringToType(String format) {
    VideoFormat[] values = VideoFormat.values();
    for (VideoFormat value : values) {
      if (value.getVideoFormat().equals(format)) {
        return value;
      }
    }
    return NONE;
  }

  public String getVideoFormat() {
    return videoFormat;
  }
}
