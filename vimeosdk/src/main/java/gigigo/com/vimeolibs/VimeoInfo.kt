package gigigo.com.vimeolibs

import com.mskn73.kache.Kacheable
import com.mskn73.kache.annotations.KacheLife
import java.io.Serializable


@KacheLife(expiresTime = 1000 * 60 * 60 * 24) // 1 day
class VimeoInfo(var id: String,
    var videoPath: String,
    var thumbPath: String,
    var isVertical: Boolean = false) : Serializable, Kacheable {

  constructor() : this("", "", "")

  override val key: String
    get() = id
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