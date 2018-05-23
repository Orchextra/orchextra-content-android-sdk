package com.gigigo.orchextra.core.data.api.mappers.video

import android.util.Log
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper
import com.gigigo.orchextra.core.data.api.dto.video.ApiVideoData
import gigigo.com.vimeolibs.VimeoInfo
import orchextra.javax.inject.Inject
import orchextra.javax.inject.Singleton

@Singleton
class ApiVideoDataMapper @Inject constructor() : ExternalClassToModelMapper<ApiVideoData, VimeoInfo> {

  override fun externalClassToModel(data: ApiVideoData): VimeoInfo {
    val time = System.currentTimeMillis()

    val model = data.element

    val currentTime = System.currentTimeMillis() - time
    Log.v("TT - ApiVideoDataMapper", ("" + currentTime / 1000))

    return model
  }
}

/*
if (videoResponse != null && videoResponse.body() != null) {
              VimeoInfo info = new VimeoInfo();
              //region  determine quality from connection
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
              //endregion

              //asv new check INDEXs, because some videos dont have all resolutions
              if (videoResponse.body().getDownload() != null
                  && videoResponse.body().files.size() < videoIdx + 1) {
                videoIdx = 0;
              }

              if (videoResponse.body().files != null
                  && videoResponse.body().files.size() >= videoIdx + 1
                  && videoResponse.body().pictures != null
                  && videoResponse.body().pictures.uri != null) {
                info.setVideoPath(videoResponse.body().files.get(videoIdx).getLink());
                Log.e("", info.getVideoPath());

                String[] split = videoResponse.body().pictures.uri.split("/");
                if(split.length >=4 ) {
                  String previewId = split[4];
                  WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                  Display display = wm.getDefaultDisplay();
                  Point size = new Point();
                  display.getSize(size);
                  int width = size.x;
                  int height = width * 9 / 16;
                  String thumbnail =
                      String.format("https://i.vimeocdn.com/video/%s_%sx%s.jpg?r=pad", previewId,
                          width, height);
                  info.setThumbPath(thumbnail);
                  Log.e("", info.getThumbPath());
                }
                if (videoResponse.body().width > videoResponse.body().height) {
                  info.setVertical(false);
                } else {
                  info.setVertical(true);
                }
                callback.onSuccess(info);
              } else {
                callback.onError(new Exception(
                    "No data retrive from Vimeo video, maybe this is not a video from provided accesstoken account"
                        + videoId));
              }
            } else {
              callback.onError(new Exception(
                  "No data retrive from Vimeo video, maybe this is not a video from provided accesstoken account"
                      + videoId));
            }
 */