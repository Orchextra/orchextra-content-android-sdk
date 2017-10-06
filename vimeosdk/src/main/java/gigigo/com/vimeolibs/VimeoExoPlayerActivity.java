package gigigo.com.vimeolibs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import java.io.File;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
  private boolean isVertical; //todo
  FrameLayout main_media_frame;
  ImageView mImageView;
  boolean isVideoLoaded = false;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.exo_player_activity);
    mImageView = (ImageView) findViewById(R.id.imgBackBlur);
    main_media_frame = (FrameLayout) findViewById(R.id.main_media_frame);

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
      isVertical = vimeoInfo.isVertical;
      if(isVertical)
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
      setBlurBackGround();
      initializeExoPlayer();
    } else {
      //todo loading, make UX, this make a leak
      progDailog = ProgressDialog.show(this, "Please wait ...", "Video Loading ...", true);
    }
  }

  ProgressDialog progDailog;

  public static void open(Context mContext, VimeoInfo info) {
    Intent i = new Intent(mContext, VimeoExoPlayerActivity.class);
    if (info != null) {
      i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
    i.putExtra(VIMEO_INFO_DATA, info);
    mContext.startActivity(i);
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
        WeakReference<Bitmap> resizedbitmap =
            new WeakReference<>(Bitmap.createBitmap(bitmap, 0, 45, 480, 270));

        BitmapDrawable ob = new BitmapDrawable(getResources(), resizedbitmap.get());
        main_media_frame.setBackground(ob);
      }
    };
    Glide.with(this)
        .load(url)
        .asBitmap()
        .transform(new BlurTransformation(VimeoExoPlayerActivity.this, 20))
        .into(mImageCallback);
  }

  public static File getCacheFile(Context mContext, String filename) {
    File cacheFile = new File(getCacheDir(mContext), filename);
    return cacheFile;
  }

  public static File getCacheDir(Context mContext) {
    // Create a path pointing to the system-recommended cache dir for the app, with sub-dir named
    // thumbnails
    File cacheDir = new File(mContext.getCacheDir(), "images");
    return cacheDir;
  }

  public static String md5(String s) {
    if (s.contains("?")) {
      int index = s.indexOf("?");
      s = s.substring(0, index);
    }
    try {
      // Create MD5 Hash
      MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
      digest.update(s.getBytes());
      byte messageDigest[] = digest.digest();

      // Create Hex String
      StringBuffer hexString = new StringBuffer();
      for (int i = 0; i < messageDigest.length; i++)
        hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return s;
  }
  //endregion

  @Override public void onConfigurationChanged(android.content.res.Configuration newConfig) {
    if (!isVertical) {
      super.onConfigurationChanged(newConfig);
    } else {
      newConfig.orientation = android.content.res.Configuration.ORIENTATION_PORTRAIT;
      super.onConfigurationChanged(null);

    }
    if (newConfig.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
      if (!isVertical) openFullscreenDialog();
    } else if (newConfig.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
      if (!isVertical) closeFullscreenDialog();
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (progDailog != null) progDailog.dismiss();
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
    }

    prepareExoPlayer();

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
    ((ViewGroup) simpleExoPlayerView.getParent()).removeView(simpleExoPlayerView);
    mFullScreenDialog.addContentView(simpleExoPlayerView,
        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT));
    mFullScreenIcon.setImageDrawable(
        ContextCompat.getDrawable(VimeoExoPlayerActivity.this, R.drawable.ic_fullscreen_skrink));
    mExoPlayerFullscreen = true;
    mFullScreenDialog.show();
  }

  private void closeFullscreenDialog() {
    ((ViewGroup) simpleExoPlayerView.getParent()).removeView(simpleExoPlayerView);
    ((FrameLayout) findViewById(R.id.main_media_frame)).addView(simpleExoPlayerView);
    mExoPlayerFullscreen = false;
    mFullScreenDialog.dismiss();
    mFullScreenIcon.setImageDrawable(
        ContextCompat.getDrawable(VimeoExoPlayerActivity.this, R.drawable.ic_fullscreen_expand));
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
  }

  @Override protected void onResume() {
    super.onResume();

    if (!TextUtils.isEmpty(vimeoLink)) {
      initializeExoPlayer();
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
        simpleExoPlayerView.getPlayer().release();
      }

      if (mFullScreenDialog != null) mFullScreenDialog.dismiss();
    }
  }
}
