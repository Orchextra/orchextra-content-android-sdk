package gigigo.com.vimeolibs;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
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
  private MediaSource mVideoSource;
  private boolean mExoPlayerFullscreen = false;
  private ImageView mFullScreenIcon;
  private Dialog mFullScreenDialog;

  private int mResumeWindow;
  private long mResumePosition;
  private String vimeoLink;
  private String thumbBackground;
  private boolean isVertical;
  FrameLayout main_media_frame;
  ImageView mImageView, mExo_fullscreen_icon;
  boolean isVideoLoaded = false;
  //ProgressDialog progDailog;
  ProgressBar mPbLoading;

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

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.exo_player_activity);

    //region fullscreen

    //Window window = getWindow();
    //WindowManager.LayoutParams winParams = window.getAttributes();
    //winParams.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
    //window.setAttributes(winParams);
    //window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    hideStatusBar();
    //endregion

    mImageView = (ImageView) findViewById(R.id.imgBackBlur);
    main_media_frame = (FrameLayout) findViewById(R.id.main_media_frame);
    mExo_fullscreen_icon = (ImageView) findViewById(R.id.exo_fullscreen_icon);

    mPbLoading = (ProgressBar) findViewById(R.id.pbLoading);

    if (savedInstanceState != null) {
      mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
      mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
      mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
      vimeoLink = savedInstanceState.getString(STATE_VIMEO_LINK);
    }

    VimeoInfo vimeoInfo = (VimeoInfo) getIntent().getSerializableExtra(VIMEO_INFO_DATA);
    if (vimeoInfo != null) {
      vimeoLink = vimeoInfo.getVideoPath();
      thumbBackground = vimeoInfo.getThumbPath();

      setBlurBackGround();
      initializeExoPlayer();
      mPbLoading.setVisibility(View.GONE);
    } else {
      //todo loading, make UX, this make a leak
      // progDailog = ProgressDialog.show(this, "", "", true);
      mPbLoading.setVisibility(View.VISIBLE);
    }
  }

  public static void open(Context mContext, VimeoInfo info) {
    if (mContext != null) {
      Intent i = new Intent(mContext, VimeoExoPlayerActivity.class);
      if (info != null) {
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      }
      i.putExtra(VIMEO_INFO_DATA, info);
      mContext.startActivity(i);
    }
  }

  private void setBlurBackGround() {
    //todo use iamgeloader ijected from OCM, but we need interface somewhere...

    if (mImageView != null) {
      loadThumbNail(thumbBackground);
    }
  }

  //region imageloader
  BitmapImageViewTarget mImageCallback;

  public void loadThumbNail(String url) {
    mImageCallback = new BitmapImageViewTarget(mImageView) {

      @Override
      public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {

        int width = 480;
        if (width > bitmap.getWidth()) {
          width = bitmap.getWidth();
        }

        int y = 45;
        int height = 270;
        if (y + height > bitmap.getHeight()) {
          height = bitmap.getHeight() - y;
        }

        WeakReference<Bitmap> resizedbitmap =
            new WeakReference<>(Bitmap.createBitmap(bitmap, 0, y, width, height));

        BitmapDrawable ob = new BitmapDrawable(getResources(), resizedbitmap.get());
        main_media_frame.setBackground(ob);
/*
        WeakReference<Bitmap> resizedbitmap =
            new WeakReference<>(Bitmap.createBitmap(bitmap, 0, 45, 480, 270));
        BitmapDrawable ob = new BitmapDrawable(getResources(), resizedbitmap.get());
        main_media_frame.setBackground(ob);
*/

      }
    };
    Glide.with(this)
        .load(url)
        .asBitmap()
        .transform(new BlurTransformation(VimeoExoPlayerActivity.this, 20))
        .into(mImageCallback);
  }

  //endregion

  @Override public void onConfigurationChanged(android.content.res.Configuration newConfig) {
    if (!isVertical) {
      hideStatusBar();
      super.onConfigurationChanged(newConfig);
    } else {
      hideStatusBar();
      newConfig.orientation = android.content.res.Configuration.ORIENTATION_PORTRAIT;
      super.onConfigurationChanged(new Configuration());
    }
    if (newConfig.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
      if (!isVertical) openFullscreenDialog();
    } else if (newConfig.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
      if (!isVertical) closeFullscreenDialog();
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    //if (progDailog != null) progDailog.dismiss();
    if (mPbLoading != null) mPbLoading = null;
    //destruir el reproductor
  }

  private void initializeExoPlayer() {
    if (simpleExoPlayerView == null) {

      simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exoplayer);
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

      mVideoSource =
          new ExtractorMediaSource(daUri, dataSourceFactory, extractorsFactory, null, null);

      prepareExoPlayer();
    }

    if (mExoPlayerFullscreen) {
      openFullscreenDialog();
    }
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
    outState.putLong(STATE_RESUME_POSITION, mResumePosition);
    outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);
    outState.putString(STATE_VIMEO_LINK, vimeoLink);

    super.onSaveInstanceState(outState);
  }

  private void initFullscreenDialog() {
    mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
      public void onBackPressed() {
        if (mExoPlayerFullscreen) {
          closeFullscreenDialog();
          setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
        super.onBackPressed();
      }
    };
  }

  private void openFullscreenDialog() {
    if (simpleExoPlayerView != null
        && simpleExoPlayerView.getParent() != null
        && mFullScreenDialog != null) {
      ((ViewGroup) simpleExoPlayerView.getParent()).removeView(simpleExoPlayerView);
      mFullScreenDialog.addContentView(simpleExoPlayerView,
          new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT));
      mFullScreenIcon.setImageDrawable(
          ContextCompat.getDrawable(VimeoExoPlayerActivity.this, R.drawable.ic_fullscreen_skrink));
      mExoPlayerFullscreen = true;
      mFullScreenDialog.show();
    }
  }

  private void closeFullscreenDialog() {
    if (simpleExoPlayerView != null
        && simpleExoPlayerView.getParent() != null
        && mFullScreenDialog != null) {
      ((ViewGroup) simpleExoPlayerView.getParent()).removeView(simpleExoPlayerView);
      ((FrameLayout) findViewById(R.id.main_media_frame)).addView(simpleExoPlayerView);
      mExoPlayerFullscreen = false;
      mFullScreenDialog.dismiss();
      mFullScreenIcon.setImageDrawable(
          ContextCompat.getDrawable(VimeoExoPlayerActivity.this, R.drawable.ic_fullscreen_expand));
    }
  }

  private void initFullscreenButton() {
    PlaybackControlView controlView = simpleExoPlayerView.findViewById(R.id.exo_controller);
    mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
    FrameLayout mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
    mFullScreenButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (!mExoPlayerFullscreen) {
          openFullscreenDialog();
          setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
          closeFullscreenDialog();
          setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
      }
    });
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
        ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this), trackSelector,
            loadControl);
    simpleExoPlayerView.setPlayer(player);

    boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;

    if (haveResumePosition) {
      simpleExoPlayerView.getPlayer().seekTo(mResumeWindow, mResumePosition);
    }

    simpleExoPlayerView.getPlayer().prepare(mVideoSource);
    simpleExoPlayerView.getPlayer().setPlayWhenReady(true);
    simpleExoPlayerView.setVisibility(View.VISIBLE);
  }

  @Override protected void onResume() {
    super.onResume();

    if (!TextUtils.isEmpty(vimeoLink)) {
      hideStatusBar();
      initializeExoPlayer();
      prepareExoPlayer();
    } else {
      //todo something ??
    }
  }

  @Override protected void onPause() {
    super.onPause();
    if (!TextUtils.isEmpty(vimeoLink)) {
      mResumeWindow = simpleExoPlayerView.getPlayer().getCurrentWindowIndex();
      mResumePosition = Math.max(0, simpleExoPlayerView.getPlayer().getContentPosition());

      if (simpleExoPlayerView != null && simpleExoPlayerView.getPlayer() != null) {

        simpleExoPlayerView.getPlayer().stop();
        simpleExoPlayerView.getPlayer().release();
        simpleExoPlayerView.getPlayer().clearVideoSurface();
      }

      if (mFullScreenDialog != null) mFullScreenDialog.dismiss();
    }
  }
}
