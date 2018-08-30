package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.orchextra.core.data.api.utils.ConnectionUtilsImp;
import com.gigigo.orchextra.core.domain.entities.article.ArticleVimeoVideoElement;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
import com.gigigo.orchextra.core.sdk.actions.ActionHandler;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.views.MoreContentArrowView;
import com.gigigo.orchextra.ocmsdk.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import gigigo.com.vimeolibs.VideoObserver;
import gigigo.com.vimeolibs.VimeoCallback;
import gigigo.com.vimeolibs.VimeoExoPlayerActivity;
import gigigo.com.vimeolibs.VimeoInfo;
import org.jetbrains.annotations.NotNull;
import timber.log.Timber;

public class ArticleVimeoVideoView extends BaseViewHolder<ArticleVimeoVideoElement> {

  private static final String TAG = "ArticleVimeoVideoView";
  private Context context;
  private MediaSource videoSource;
  private ConnectionUtils connectionUtils;
  private ActionHandler actionHandler;
  private ImageView imgPlay;
  private ImageView imgThumb;
  private View videoPlayerSurface;
  private SimpleExoPlayerView videoPlayer;
  private VimeoInfo mVimeoInfo;

  public ArticleVimeoVideoView(Context context, ViewGroup parent, ActionHandler actionHandler) {
    super(context, parent, R.layout.view_article_video_item_vimeo);
    connectionUtils = new ConnectionUtilsImp(context);

    this.context = context;
    imgPlay = itemView.findViewById(R.id.imgPlay);
    imgThumb = itemView.findViewById(R.id.imgThumb);
    videoPlayerSurface = itemView.findViewById(R.id.videoPlayerSurface);
    this.actionHandler = actionHandler;
  }

  @Override public void bindTo(ArticleVimeoVideoElement articleElement, int position) {
    //todo truchingvimeo

    videoPlayerSurface.setOnClickListener(onVimeoThumbnailClickListener);

    actionHandler.getVimeoInfo(articleElement.getRender().getSource(),
        new VideoObserver(new VimeoCallback() {
          @Override public void onSuccess(@NotNull VimeoInfo vimeoInfo) {
            mVimeoInfo = vimeoInfo;
            String strImgForBlur = mVimeoInfo.getThumbPath();

            imgThumb.setMaxHeight(
                (int) MoreContentArrowView.convertDpToPixel(192, context.getApplicationContext()));
            imgThumb.setScaleType(ImageView.ScaleType.FIT_CENTER);

            Glide.with(context.getApplicationContext())

                .load(strImgForBlur)
                //.asBitmap()
                // .transform(new BlurTransformation(context, 20))
                .listener(new RequestListener<String, GlideDrawable>() {
                  @Override public boolean onException(Exception e, String model,
                      Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                  }

                  @Override public boolean onResourceReady(GlideDrawable resource, String model,
                      Target<GlideDrawable> target, boolean isFromMemoryCache,
                      boolean isFirstResource) {
                    imgPlay.setVisibility(View.VISIBLE);

                    if (!isPlay) {
                      isPlay = true;
                      playVideoPreview(vimeoInfo.getVideoPath());
                    }

                    return false;
                  }
                })

                .into(imgThumb);
            //imgPlay.setOnClickListener(onVimeoThumbnailClickListener);
            //imgThumb.setOnClickListener(onVimeoThumbnailClickListener);
          }

          @Override public void onError(@NotNull Throwable e) {
            Timber.e(e, "getVimeoInfo()");
          }
        }));
  }

  public void playVideoPreview(String vimeoLink) {

    initializeExoPlayer(vimeoLink);

    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
    TrackSelection.Factory videoTrackSelectionFactory =
        new AdaptiveTrackSelection.Factory(bandwidthMeter);
    TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
    LoadControl loadControl = new DefaultLoadControl();

    if (videoPlayer.getPlayer() != null) {
      videoPlayer.getPlayer().stop();
      videoPlayer.getPlayer().release();
    }
    SimpleExoPlayer player =
        ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(context), trackSelector,
            loadControl);
    videoPlayer.setPlayer(player);
    videoPlayer.getPlayer().setVolume(0);

    videoPlayer.getPlayer().prepare(videoSource);
    videoPlayer.getPlayer().setPlayWhenReady(true);
    videoPlayer.setVisibility(View.VISIBLE);
    videoPlayer.getPlayer().setRepeatMode(Player.REPEAT_MODE_ALL);
    videoPlayer.hideController();

    imgThumb.setVisibility(View.INVISIBLE);
    imgPlay.setVisibility(View.INVISIBLE);
  }

  private void initializeExoPlayer(String vimeoLink) {
    if (videoPlayer == null) {

      videoPlayer = itemView.findViewById(R.id.videoPlayer);

      String userAgent = Util.getUserAgent(context, context.getApplicationInfo().packageName);
      DefaultHttpDataSourceFactory httpDataSourceFactory =
          new DefaultHttpDataSourceFactory(userAgent, null,
              DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
              DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true);
      DefaultDataSourceFactory dataSourceFactory =
          new DefaultDataSourceFactory(context, null, httpDataSourceFactory);

      Uri daUri = Uri.parse(vimeoLink);

      ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

      videoSource =
          new ExtractorMediaSource(daUri, dataSourceFactory, extractorsFactory, null, null);
    }
  }

  public void stopVideoPreview() {
    if (videoPlayer != null && videoPlayer.getPlayer() != null) {
      videoPlayer.getPlayer().stop();
      videoPlayer.getPlayer().release();
      videoPlayer.getPlayer().clearVideoSurface();
    }

    imgThumb.setVisibility(View.VISIBLE);
    imgPlay.setVisibility(View.VISIBLE);
  }

  private View.OnClickListener onVimeoThumbnailClickListener = v -> {
    if (connectionUtils.hasConnection()) {
      if (mVimeoInfo != null) VimeoExoPlayerActivity.open(context, mVimeoInfo);
    } else {
      OCManager.getCustomTranslation(R.string.oc_error_content_not_available_without_internet,
          text -> {

            if (text != null) {
              Snackbar.make(imgThumb, text, Snackbar.LENGTH_SHORT).show();
            } else {
              Snackbar.make(imgThumb, R.string.oc_error_content_not_available_without_internet,
                  Snackbar.LENGTH_SHORT).show();
            }
            return null;
          });
    }
  };

  private static boolean isPlay = false;
}
