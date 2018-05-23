package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.gigigo.ggglib.device.AndroidSdkVersion;
import com.gigigo.orchextra.core.sdk.model.grid.dto.ClipToPadding;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import com.gigigo.orchextra.ocmsdk.BuildConfig;
import com.gigigo.orchextra.ocmsdk.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import java.lang.ref.WeakReference;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class YoutubeFragment extends UiGridBaseContentData {

  private static final String EXTRA_PLAYED_VIDEO = "EXTRA_PLAYED_VIDEO";
  private static final String EXTRA_IS_PLAYING = "EXTRA_IS_PLAYING";
  private static final String YOUTUBE_FRAGMENT = "YOUTUBE_FRAGMENT";
  private static final String EXTRA_YOUTUBE_ID = "EXTRA_YOUTUBE_ID";
  private static final String EXTRA_YOUTUBE_ORIENTATION = "EXTRA_YOUTUBE_ORIENTATION";

  private static final String TAG = YoutubeFragment.class.getSimpleName();

  private RelativeLayout youtubeLayoutContainer;
  private ImageView imgBlurBackground;

  private String youtubeId;
  private int orientation;
  private BitmapImageViewTarget mImageCallback;
  private int playedVideo;
  private boolean isPlaying;

  YouTubePlayer.OnInitializedListener onInitializedListener =
      new YouTubePlayer.OnInitializedListener() {

        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
            boolean wasRestored) {

          if (!wasRestored) {
            setYouTubePlayer(player);
          }
        }

        @Override public void onInitializationFailure(YouTubePlayer.Provider provider,
            YouTubeInitializationResult error) {
        }
      };
  private Boolean isVertical;

  public int getScreenOrientation() {
    Display getOrient = getActivity().getWindowManager().getDefaultDisplay();
    int orientation;
    if (getOrient.getWidth() == getOrient.getHeight()) {
      orientation = Configuration.ORIENTATION_SQUARE;
    } else {
      if (getOrient.getWidth() < getOrient.getHeight()) {
        orientation = Configuration.ORIENTATION_PORTRAIT;
      } else {
        orientation = Configuration.ORIENTATION_LANDSCAPE;
      }
    }
    return orientation;
  }

  public static YoutubeFragment newInstance(String youtubeId, int orientation) {
    YoutubeFragment youtubeElements = new YoutubeFragment();

    Bundle bundle = new Bundle();
    bundle.putString(EXTRA_YOUTUBE_ID, youtubeId);
    bundle.putInt(EXTRA_YOUTUBE_ORIENTATION, orientation);
    youtubeElements.setArguments(bundle);

    return youtubeElements;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setRetainInstance(true);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View mView = inflater.inflate(R.layout.view_youtube_elements_item, container, false);

    initViews(mView);

    mImageCallback = new BitmapImageViewTarget(imgBlurBackground) {

      @Override
      public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
        WeakReference<Bitmap> resizedbitmap =
            new WeakReference<>(Bitmap.createBitmap(bitmap, 0, 45, 480, 270));
        imgBlurBackground.setImageBitmap(resizedbitmap.get());

        boolean isVertical = false;
        int midleImage = bitmap.getHeight() / 2;
        for (int i = 0; i < 20; i++) {
          int pixel = bitmap.getPixel(i, midleImage);
          int r = Color.red(pixel);
          int g = Color.green(pixel);
          int b = Color.blue(pixel);
          if (r == 0 && g == 0 && b == 0) {
            isVertical = true;
          } else {
            isVertical = false;
            break;
          }
        }

        initYoutubeFragment(isVertical, mView);
      }
    };

    initThumbnail();

    return mView;
  }

  private void initViews(View view) {
    youtubeLayoutContainer = (RelativeLayout) view.findViewById(R.id.youtubeLayoutContainer);

    imgBlurBackground = (ImageView) view.findViewById(R.id.imgBlurBackground);

    youtubeLayoutContainer.getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @TargetApi(Build.VERSION_CODES.JELLY_BEAN) @Override public void onGlobalLayout() {
            FrameLayout.LayoutParams lp =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);

            youtubeLayoutContainer.setLayoutParams(lp);
            if (AndroidSdkVersion.hasJellyBean16()) {
              youtubeLayoutContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
          }
        });
  }

  private void initThumbnail() {
    youtubeId = getArguments().getString(EXTRA_YOUTUBE_ID);

    String strImgForBlur = "http://img.youtube.com/vi/" + youtubeId + "/hqdefault.jpg";
    Glide.with(this)
        .load(strImgForBlur)
        .asBitmap()
        .transform(new BlurTransformation(YoutubeFragment.this.getActivity(), 20))
        .into(mImageCallback);
  }

  private void initYoutubeFragment(Boolean isVertical, View mView) {
    this.isVertical = isVertical;
    try {
      orientation = getArguments().getInt(EXTRA_YOUTUBE_ORIENTATION);

      if (!TextUtils.isEmpty(youtubeId)) {
        Log.e("+++", "TextUtils.isEmpty(youtubeId) " + youtubeId + "orientation" + orientation);
        if (isVertical || orientation == Configuration.ORIENTATION_LANDSCAPE) {
          //Toast.makeText(YoutubeFragment.this.getActivity(), "ES VERTICAL", Toast.LENGTH_LONG).show();
          setYoutubeFragmentToView(mView, LinearLayout.LayoutParams.MATCH_PARENT);
          if (isVertical) {
            Log.d(TAG, "video is PORTRAIT");
            YoutubeFragment.this.getActivity()
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
          }
        } else {
          //Toast.makeText(YoutubeFragment.this.getActivity(), "ES HORIZONTAL", Toast.LENGTH_LONG).show();
          Log.d(TAG, "video is LANDSCAPE");
          setYoutubeFragmentToView(mView, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
      }


      YouTubePlayerFragment youTubePlayerFragment2 = YouTubePlayerFragment.newInstance();
      youTubePlayerFragment2.initialize(BuildConfig.YOUTUBE_DEVELOPER_KEY, onInitializedListener);

      if (this.getActivity() != null && !this.getActivity().isFinishing()) {
        getChildFragmentManager().beginTransaction()
            .replace(R.id.youtubePlayerFragmentContent, youTubePlayerFragment2, YOUTUBE_FRAGMENT)
            .commitAllowingStateLoss();
      }
    } catch (Exception ignored) {
    }
  }

  private void setYoutubeFragmentToView(View mView, int h) {

    // Gets linearlayout
    FrameLayout layout = mView.findViewById(R.id.youtubePlayerFragmentContent);
    // Gets the layout params that will allow you to resize the layout
    ViewGroup.LayoutParams params = layout.getLayoutParams();
    Log.e("***", "************************\n\n\n\n\n\n\n PASO************************");

    params.height = h; //LinearLayout.LayoutParams.WRAP_CONTENT.;
    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
    layout.setLayoutParams(params);
  }

  public void setYouTubePlayer(final YouTubePlayer player) {
    try {
      if (player == null) {
        return;
      }

      if (this.isVertical) {
        player.setShowFullscreenButton(false);
      } else {
        player.setShowFullscreenButton(true);
      }
      player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

      if (playedVideo >= 0) {
        if (playedVideo == 0 || isPlaying) {
          player.loadVideo(youtubeId, playedVideo);
        } else {
          player.cueVideo(youtubeId, playedVideo);
        }
      }
    } catch (Exception ignored) {
    }
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    if (savedInstanceState != null) {
      playedVideo = savedInstanceState.getInt(EXTRA_PLAYED_VIDEO);
      isPlaying = savedInstanceState.getBoolean(EXTRA_IS_PLAYING);
    }
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    try {
      YouTubePlayerFragment youTubePlayerSupportFragment =
          (YouTubePlayerFragment) getChildFragmentManager().findFragmentByTag(YOUTUBE_FRAGMENT);
      YouTubePlayer mPlayer = youTubePlayerSupportFragment.getPlayer();

      if (mPlayer != null) {
        outState.putInt(EXTRA_PLAYED_VIDEO, mPlayer.getCurrentTimeMillis());
        outState.putBoolean(EXTRA_IS_PLAYING, mPlayer.isPlaying());
      }
    } catch (Exception ignored) {
    }

    super.onSaveInstanceState(outState);
  }

  @Override public void setFilter(String filter) {

  }

  @Override public void setClipToPaddingBottomSize(ClipToPadding clipToPadding,
      int addictionalPadding) {

  }

  @Override public void scrollToTop() {

  }

  @Override public void setEmptyView(View emptyView) {

  }

  @Override public void setErrorView(View errorLayoutView) {

  }

  @Override public void setProgressView(View progressView) {

  }

  @Override public void reloadSection(boolean hasToShowNewContentButton) {

  }
}
