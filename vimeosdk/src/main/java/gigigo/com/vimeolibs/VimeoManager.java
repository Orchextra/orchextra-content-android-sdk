package gigigo.com.vimeolibs;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import com.vimeo.networking.Configuration;
import com.vimeo.networking.VimeoClient;
import com.vimeo.networking.model.Video;
import okhttp3.CacheControl;
import retrofit2.Response;

public class VimeoManager {
  private static String noket_ssecca = "";
  private static String clientID = "";
  private static String clientSecret = "";
  private static String scope = "";
  private static VimeoClient vimeoApiClient;

  public VimeoManager(VimeoBuilder builder) {
    Configuration.Builder configBuilder;
    if (builder != null) {
      noket_ssecca = builder.getToken();
      clientID = builder.getClientId();
      clientSecret = builder.getClientSecret();
      scope = builder.getScope();

      if (noket_ssecca != null && !noket_ssecca.equals("")) {
        configBuilder = new Configuration.Builder(noket_ssecca);
        VimeoClient.initialize(configBuilder.build());
        vimeoApiClient = VimeoClient.getInstance();
      } else {
        if (clientID != null
            && !clientID.equals("")
            && clientSecret != null
            && !clientSecret.equals("")
            && scope != null
            && !scope.equals("")) {

          configBuilder = new Configuration.Builder(clientID, clientSecret, scope);
          VimeoClient.initialize(configBuilder.build());
          vimeoApiClient = VimeoClient.getInstance();
        }
      }
    }
  }

  public void getVideoVimeoInfo(final Context context, final String videoId, final boolean isWifiConnection,
      final boolean isFastConnection, final VimeoCallback callback) {

    final Response<Video> videoResponse =
            vimeoApiClient.fetchVideoSync("/videos/" + videoId, CacheControl.FORCE_NETWORK, "pictures.uri,files");


        new Handler(Looper.getMainLooper()).post(new Runnable() {
          @Override public void run() {
            if (videoResponse != null && videoResponse.body() != null) {
              VimeoInfo info = new VimeoInfo();
              info.setId(videoId);

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
          }
        });
  }
}
