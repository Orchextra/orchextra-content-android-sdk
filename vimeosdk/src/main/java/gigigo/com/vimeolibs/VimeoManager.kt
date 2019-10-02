package gigigo.com.vimeolibs

import android.content.Context
import android.graphics.Point
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.vimeo.networking.Configuration
import com.vimeo.networking.VimeoClient
import com.vimeo.networking.model.Video
import com.vimeo.networking.model.VideoFile
import java.util.AbstractList
import java.util.ArrayList
import okhttp3.CacheControl
import retrofit2.Response
import timber.log.Timber

class VimeoManager(builder: VimeoBuilder?) {

  init {
    val configBuilder: Configuration.Builder
    if (builder != null) {
      noket_ssecca = builder.token
      clientID = builder.clientId
      clientSecret = builder.clientSecret
      scope = builder.scope

      if (noket_ssecca != null && noket_ssecca != "") {
        configBuilder = Configuration.Builder(noket_ssecca)
        VimeoClient.initialize(configBuilder.build())
        vimeoApiClient = VimeoClient.getInstance()
      } else {
        if (clientID != null
            && clientID != ""
            && clientSecret != null
            && clientSecret != ""
            && scope != null
            && scope != ""
        ) {

          configBuilder = Configuration.Builder(clientID, clientSecret, scope)
          VimeoClient.initialize(configBuilder.build())
          vimeoApiClient = VimeoClient.getInstance()
        }
      }
    }
  }

  fun getVideoVimeoInfo(
    context: Context,
    videoId: String,
    isWifiConnection: Boolean,
    isFastConnection: Boolean,
    callback: VimeoCallback
  ) {

    val videoResponse = vimeoApiClient!!.fetchVideoSync(
        "/videos/$videoId", CacheControl.FORCE_NETWORK,
        "pictures.uri,files"
    )

    Handler(Looper.getMainLooper()).post {
      if (videoResponse?.body() != null) {
        val info = VimeoInfo()
        info.id = videoId

        if (videoResponse.body()!!.files != null
            && videoResponse.body()!!.pictures != null
            && videoResponse.body()!!.pictures.uri != null
        ) {

          info.videoPath =
              getLinkForNetworkQuality(videoResponse, isWifiConnection, isFastConnection)

          val split = videoResponse.body()!!
              .pictures.uri.split("/".toRegex())
              .dropLastWhile { it.isEmpty() }
              .toTypedArray()
          if (split.size >= 4) {
            val previewId = split[4]
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            val width = size.x
            val height = width * 9 / 16
            val thumbnail = String.format(
                "https://i.vimeocdn.com/video/%s_%sx%s.jpg?r=pad", previewId,
                width, height
            )
            info.thumbPath = thumbnail
          }

          callback.onSuccess(info)
        } else {
          callback.onError(
              Exception(
                  "No data retrive from Vimeo video, maybe this is not a video from provided accesstoken account$videoId"
              )
          )
        }
      } else {
        callback.onError(
            Exception(
                "No data retrive from Vimeo video, maybe this is not a video from provided accesstoken account$videoId"
            )
        )
      }
    }
  }

  private fun getLinkForNetworkQuality(
    videoResponse: Response<Video>?,
    isWifiConnection: Boolean,
    isFastConnection: Boolean
  ): String {
      val files = videoResponse?.body()?.download
      if(files == null) {
          Timber.e("video files empty")
          return ""
      }
      return when {
          isFastConnection &&  isWifiConnection -> getFullQualityVideo(files)
          isFastConnection && !isWifiConnection -> getHdReadyVideo(files)
          !isFastConnection &&  isWifiConnection -> getSdVideo(files)
          else -> getLowQualityVideo(files)
      }
  }

  private fun getFilteredByQualityVideos(
    files: List<VideoFile>,
    quality: String = "sd"
  ): Sequence<VideoFile> {
    return files
        .asSequence()
        .filter {
          it.quality.toString() == quality && it.quality.toString() != "hls"
        }
  }

  private fun returnDefaultVideo(files: List<VideoFile>) =
    if (files.isNotEmpty()) files[0].link else ""

  private fun getFullQualityVideo(files: List<VideoFile>): String {
    return getFilteredByQualityVideos(files, "hd")
        .maxBy { it.getSize() }?.link ?: returnDefaultVideo(files)
  }

  private fun getHdReadyVideo(files: AbstractList<VideoFile>): String {
    return getFilteredByQualityVideos(files, "hd")
        .minBy { it.getSize() }?.link ?: returnDefaultVideo(files)
  }

  private fun getSdVideo(files: ArrayList<VideoFile>): String {
    return getFilteredByQualityVideos(files, "sd")
        .maxBy { it.getSize() }?.link ?: returnDefaultVideo(files)
  }

  private fun getLowQualityVideo(files: ArrayList<VideoFile>): String {
    return getFilteredByQualityVideos(files, "sd")
        .minBy { it.getSize() }?.link ?: returnDefaultVideo(files)
  }

  companion object {
    private var noket_ssecca: String? = ""
    private var clientID: String? = ""
    private var clientSecret: String? = ""
    private var scope: String? = ""
    private var vimeoApiClient: VimeoClient? = null
  }
}
