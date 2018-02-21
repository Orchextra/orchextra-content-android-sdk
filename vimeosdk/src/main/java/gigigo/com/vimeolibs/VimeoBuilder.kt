package gigigo.com.vimeolibs

class VimeoBuilder(token: String) {
  var token = ""
  var clientId = ""
  var clientSecret = ""
  var scope = "private public video_files"

  init {
    this.token = token
  }
}