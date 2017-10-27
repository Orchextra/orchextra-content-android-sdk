package gigigo.com.vimeolibs;

import java.io.Serializable;

/**
 * Created by nubor on 04/10/2017.
 */

public class VimeoInfo implements Serializable {
  String videoPath;
  String thumbPath;
  boolean isVertical;

  public VimeoInfo() {
  }

  public VimeoInfo(String videoPath, String thumbPath, boolean isVertical) {
    this.videoPath = videoPath;
    this.thumbPath = thumbPath;
    this.isVertical = isVertical;
  }

  public String getVideoPath() {
    return videoPath;
  }

  public void setVideoPath(String videoPath) {
    this.videoPath = videoPath;
  }

  public String getThumbPath() {
    return thumbPath;
  }

  public void setThumbPath(String thumbPath) {
    this.thumbPath = thumbPath;
  }

  public boolean isVertical() {
    return isVertical;
  }

  public void setVertical(boolean vertical) {
    isVertical = vertical;
  }
}