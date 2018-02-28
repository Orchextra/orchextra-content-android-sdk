package gigigo.com.vimeolibs

import io.reactivex.observers.DisposableObserver

class VideoObserver(
    private val getVideoCallback: VimeoCallback?) : DisposableObserver<VimeoInfo>() {
  override fun onComplete() {
  }

  override fun onNext(vimeoInfo: VimeoInfo) {
    getVideoCallback?.onSuccess(vimeoInfo)
  }

  override fun onError(e: Throwable) {
    getVideoCallback?.onError(Exception(e))
    e.printStackTrace()
  }
}