package gigigo.com.vimeolibs;


public final class VideoObserver extends DefaultObserver<VimeoInfo> {

  private final VimeoCallback getVideoCallback;

  public VideoObserver(VimeoCallback getVideoCallback) {
    this.getVideoCallback = getVideoCallback;
  }

  @Override public void onComplete() {

  }

  @Override public void onNext(VimeoInfo vimeoInfo) {
    if (getVideoCallback != null) {
      getVideoCallback.onSuccess(vimeoInfo);
    }
  }

  @Override public void onError(Throwable e) {
    if (getVideoCallback != null) {
      getVideoCallback.onError(new Exception(e));
    }
    e.printStackTrace();
  }
}