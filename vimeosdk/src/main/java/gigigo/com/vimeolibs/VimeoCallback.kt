package gigigo.com.vimeolibs

interface VimeoCallback {
  fun onSuccess(videoData: VimeoInfo)
  fun onError(e: Throwable)
}
