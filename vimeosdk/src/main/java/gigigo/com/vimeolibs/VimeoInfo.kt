package gigigo.com.vimeolibs

import java.io.Serializable

class VimeoInfo(var id: String,
    var videoPath: String,
    var thumbPath: String,
    var isVertical: Boolean = false) : Serializable {

  constructor() : this("", "", "")
}

/*
public class VimeoInfo implements Serializable {
  String id;
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

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

 */