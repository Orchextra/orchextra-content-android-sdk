package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.vimeo;

import android.os.AsyncTask;
import com.vimeo.networking.Configuration;
import com.vimeo.networking.VimeoClient;
import com.vimeo.networking.model.Video;
import okhttp3.CacheControl;
import retrofit2.Response;

public class GetVimeoVideoTask extends AsyncTask<Void, Void, Response<Video>> {

  private final String videoId;
  private final boolean isWifiConnection;
  private final boolean isFastConnection;
  private final VimeoCallback callback;

  private VimeoClient mApiClient;

  GetVimeoVideoTask(String accessToken, String videoId, boolean isWifiConnection,
      boolean isFastConnection, VimeoCallback callback) {

    this.videoId = videoId;
    this.isWifiConnection = isWifiConnection;
    this.isFastConnection = isFastConnection;
    this.callback = callback;

    Configuration.Builder configBuilder = new Configuration.Builder(accessToken);

    VimeoClient.initialize(configBuilder.build());

    mApiClient = VimeoClient.getInstance();
  }

  @Override protected Response<Video> doInBackground(Void... strings) {
    return mApiClient.fetchVideoSync("/videos/" + videoId, CacheControl.FORCE_NETWORK, null);
  }

  @Override protected void onPostExecute(Response<Video> videoResponse) {
    VimeoInfo info = new VimeoInfo();

    int videoIdx = getVideoPathIndex();

    if (videoResponse != null && videoResponse.body() != null) {
      Video body = videoResponse.body();

      if (body != null && body.getDownload() != null && videoIdx < body.getDownload().size()) {
        info.setVideoPath(body.getDownload().get(videoIdx).getLink());
      }

      if (body.pictures != null
          && body.pictures.sizes != null
          && videoIdx < body.pictures.sizes.size()) {
        info.setThumbnailPath(body.pictures.sizes.get(videoIdx).link);
      }

      info.setVertical(false);
      callback.onSuccess(info);

      return;
    }

    callback.onError();
  }

  private int getVideoPathIndex() {
    int videoIdx;

    if (isFastConnection) {
      if (isWifiConnection) {
        videoIdx = VimeoQuality.HDFULL.ordinal();
      } else {
        videoIdx = VimeoQuality.HDREADY.ordinal();
      }
    } else {
      if (isWifiConnection) {
        videoIdx = VimeoQuality.SD.ordinal();
      } else {
        videoIdx = VimeoQuality.SDLOW.ordinal();
      }
    }
    return videoIdx;
  }
}
