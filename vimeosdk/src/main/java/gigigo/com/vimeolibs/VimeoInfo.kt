package gigigo.com.vimeolibs

import java.io.Serializable

class VimeoInfo(var id: String,
    var videoPath: String,
    var thumbPath: String,
    var isVertical: Boolean = false) : Serializable {

  constructor() : this("", "", "")
}
