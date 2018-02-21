package gigigo.com.vimeolibs;

import java.io.Serializable;

/**
 * Created by nubor on 04/10/2017.
 */

public class VimeoInfo implements Serializable {
  String videoPath;
  String thumbPath;

  public VimeoInfo() {
  }

  public VimeoInfo(String videoPath, String thumbPath) {
    this.videoPath = videoPath;
    this.thumbPath = thumbPath;
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
}
