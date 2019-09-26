package gigigo.com.vimeolibs;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import java.lang.ref.WeakReference;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class VimeoExoPlayerActivity extends AppCompatActivity {

  private final String STATE_RESUME_WINDOW = "resumeWindow";
  private final String STATE_RESUME_POSITION = "resumePosition";
  private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";
  private final String STATE_VIMEO_LINK = "vimeLink";
  private static final String VIMEO_INFO_DATA = "VIMEO_INFO_DATA";

  private SimpleExoPlayerView simpleExoPlayerView;
  private MediaSource videoSource;
  private boolean isInFullscreen = false;
  private ImageView fullScreenIcon;
  private Dialog fullScreenDialog;

  private int resumeWindow;
  private long resumePosition;
  private String vimeoLink;
  private String thumbBackground;
  FrameLayout mainMediaFrame;
  ImageView backgroundImage;
  ProgressBar progressBar;

  private static VimeoExoPlayerActivity instance;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    instance = this;

    setContentView(R.layout.exo_player_activity);

    hideStatusBar();

    backgroundImage = findViewById(R.id.background_image);
    mainMediaFrame = findViewById(R.id.main_media_frame);
    progressBar = findViewById(R.id.progress_bar);

    if (savedInstanceState != null) {
      resumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
      resumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
      isInFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
      vimeoLink = savedInstanceState.getString(STATE_VIMEO_LINK);
    }

    VimeoInfo vimeoInfo = (VimeoInfo) getIntent().getSerializableExtra(VIMEO_INFO_DATA);
    if (vimeoInfo != null) {
      play(vimeoInfo);
    } else {
      progressBar.setVisibility(View.VISIBLE);
    }
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    outState.putInt(STATE_RESUME_WINDOW, resumeWindow);
    outState.putLong(STATE_RESUME_POSITION, resumePosition);
    outState.putBoolean(STATE_PLAYER_FULLSCREEN, isInFullscreen);
    outState.putString(STATE_VIMEO_LINK, vimeoLink);

    super.onSaveInstanceState(outState);
  }

  @Override public void onConfigurationChanged(android.content.res.Configuration newConfig) {
    hideStatusBar();
    super.onConfigurationChanged(newConfig);

    if (newConfig.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
      openFullscreen();
    } else if (newConfig.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
      closeFullscreen();
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    instance = null;
    if (progressBar != null) progressBar = null;
  }

  @Override protected void onResume() {
    super.onResume();

    if (!TextUtils.isEmpty(vimeoLink)) {
      hideStatusBar();
      initializeExoPlayer();
      prepareExoPlayer();
    }
  }

  @Override protected void onPause() {
    super.onPause();
    if (!TextUtils.isEmpty(vimeoLink)) {
      resumeWindow = simpleExoPlayerView.getPlayer().getCurrentWindowIndex();
      resumePosition = Math.max(0, simpleExoPlayerView.getPlayer().getContentPosition());

      if (simpleExoPlayerView != null && simpleExoPlayerView.getPlayer() != null) {
        simpleExoPlayerView.getPlayer().stop();
        simpleExoPlayerView.getPlayer().release();
        ((SimpleExoPlayer)simpleExoPlayerView.getPlayer()).clearVideoSurface();
      }

      if (fullScreenDialog != null) fullScreenDialog.dismiss();
    }
  }

  public static void open(Context context, VimeoInfo vimeoInfo) {
    if (context != null) {
      Intent i = new Intent(context, VimeoExoPlayerActivity.class);
      if (vimeoInfo != null) {
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      }
      i.putExtra(VIMEO_INFO_DATA, vimeoInfo);
      context.startActivity(i);
    }
  }

  public static void openVideo(Context context, VimeoInfo vimeoInfo) {
    if (instance == null) {
      open(context, vimeoInfo);
    } else {
      instance.play(vimeoInfo);
    }
  }

  private void play(VimeoInfo vimeoInfo) {
    if (vimeoInfo != null) {
      vimeoLink = vimeoInfo.getVideoPath();
      thumbBackground = vimeoInfo.getThumbPath();

      loadBackgroundThumbnail();
      initializeExoPlayer();
      progressBar.setVisibility(View.GONE);
    } else {
      progressBar.setVisibility(View.VISIBLE);
    }
  }

  private void hideStatusBar() {
    try {
      View decorView = getWindow().getDecorView();
      // Hide the status bar.
      int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
      decorView.setSystemUiVisibility(uiOptions);
      // Remember that you should never show the action bar if the
      // status bar is hidden, so hide that too if necessary.
      ActionBar actionBar = getActionBar();
      if (actionBar != null) actionBar.hide();
    } catch (Throwable throwable) {

    }
  }

  public void loadBackgroundThumbnail() {
    if (backgroundImage != null) {
      BitmapImageViewTarget  mImageCallback = new BitmapImageViewTarget(backgroundImage) {
        @Override
        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
          int width = 480;
          if (width > bitmap.getWidth()) {
            width = bitmap.getWidth();
          }

          int y = 45;
          int height = 270;
          if (y + height > bitmap.getHeight()) {
            height = bitmap.getHeight() - y;
          }

          WeakReference<Bitmap> resizedBitmap =
              new WeakReference<>(Bitmap.createBitmap(bitmap, 0, y, width, height));

          BitmapDrawable ob = new BitmapDrawable(getResources(), resizedBitmap.get());
          mainMediaFrame.setBackground(ob);
        }
      };

      Glide.with(this)
              .asBitmap()
              .load(thumbBackground)
          .transform(new BlurTransformation(VimeoExoPlayerActivity.this, 20))
          .into(mImageCallback);
    }
  }

  private void initializeExoPlayer() {
    if (simpleExoPlayerView == null) {

      simpleExoPlayerView = findViewById(R.id.exoplayer_view);
      initFullscreenDialog();
      initFullscreenButton();

      String userAgent = Util.getUserAgent(VimeoExoPlayerActivity.this,
          getApplicationContext().getApplicationInfo().packageName);
      DefaultHttpDataSourceFactory httpDataSourceFactory =
          new DefaultHttpDataSourceFactory(userAgent, null,
              DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
              DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true);
      DefaultDataSourceFactory dataSourceFactory =
          new DefaultDataSourceFactory(VimeoExoPlayerActivity.this, null, httpDataSourceFactory);

      Uri daUri = Uri.parse(vimeoLink);

      ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

      videoSource = new ExtractorMediaSource(daUri, dataSourceFactory, extractorsFactory, null, null);

      prepareExoPlayer();
    }

    if (isInFullscreen) {
      openFullscreen();
    }
  }

  private void initFullscreenDialog() {
    fullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
      public void onBackPressed() {
        if (isInFullscreen) {
          closeFullscreen();
          setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
        super.onBackPressed();
      }
    };
  }

  private void initFullscreenButton() {
    PlaybackControlView controlView = simpleExoPlayerView.findViewById(R.id.exo_controller);
    fullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
    FrameLayout fullscreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
    fullscreenButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (!isInFullscreen) {
          openFullscreen();

        } else {
          closeFullscreen();
        }
      }
    });
  }

  private void openFullscreen() {
    if (simpleExoPlayerView != null && simpleExoPlayerView.getParent() != null && fullScreenDialog != null) {
      ((ViewGroup) simpleExoPlayerView.getParent()).removeView(simpleExoPlayerView);
      fullScreenDialog.addContentView(simpleExoPlayerView,
          new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT));
      fullScreenIcon.setImageResource(R.drawable.ic_fullscreen_skrink);
      isInFullscreen = true;
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
      fullScreenDialog.show();
    }
  }

  private void closeFullscreen() {
    if (simpleExoPlayerView != null && simpleExoPlayerView.getParent() != null && fullScreenDialog != null) {
      ((ViewGroup) simpleExoPlayerView.getParent()).removeView(simpleExoPlayerView);
      ((FrameLayout) findViewById(R.id.main_media_frame)).addView(simpleExoPlayerView);
      isInFullscreen = false;
      fullScreenIcon.setImageResource(R.drawable.ic_fullscreen_expand);
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
      fullScreenDialog.dismiss();
    }
  }

  private void prepareExoPlayer() {
    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
    TrackSelection.Factory videoTrackSelectionFactory =
        new AdaptiveTrackSelection.Factory(bandwidthMeter);
    TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
    LoadControl loadControl = new DefaultLoadControl();

    if (simpleExoPlayerView.getPlayer() != null) {
      simpleExoPlayerView.getPlayer().stop();
      simpleExoPlayerView.getPlayer().release();
    }
    SimpleExoPlayer player =
        ExoPlayerFactory.newSimpleInstance(this,new DefaultRenderersFactory(this), trackSelector,
            loadControl);
    simpleExoPlayerView.setPlayer(player);

    boolean haveResumePosition = resumeWindow != C.INDEX_UNSET;

    if (haveResumePosition) {
      simpleExoPlayerView.getPlayer().seekTo(resumeWindow, resumePosition);
    }

    ((SimpleExoPlayer) simpleExoPlayerView.getPlayer()).prepare(videoSource);
    simpleExoPlayerView.getPlayer().setPlayWhenReady(true);
    simpleExoPlayerView.setVisibility(View.VISIBLE);
  }
}
