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
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.ocmsdk.BuildConfig;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.glide.transformations.BlurTransformation;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;

public class YoutubeFragment extends UiBaseContentData {

  private static final String EXTRA_PLAYED_VIDEO = "EXTRA_PLAYED_VIDEO";
  private static final String EXTRA_IS_PLAYING = "EXTRA_IS_PLAYING";
  private static final String YOUTUBE_FRAGMENT = "YOUTUBE_FRAGMENT";
  private static final String EXTRA_YOUTUBE_ID = "EXTRA_YOUTUBE_ID";
  private static final String EXTRA_YOUTUBE_ORIENTARION = "EXTRA_YOUTUBE_ORIENTARION";

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
          Log.e("+++:", "onInitializationFailure" + error.toString());
        }
      };

  public static YoutubeFragment newInstance(String youtubeId, int orientation) {
    YoutubeFragment youtubeElements = new YoutubeFragment();

    Log.e("+++", "YoutubeFragment newInstance" + youtubeId + "orientation" + orientation);

    Bundle bundle = new Bundle();
    bundle.putString(EXTRA_YOUTUBE_ID, youtubeId);
    bundle.putInt(EXTRA_YOUTUBE_ORIENTARION, orientation);
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

    Log.e("+++",
        "+++++++++++++++++++++++++++++\n\n\n\n\n\n\n createview +++++++++++++++++++++++++++++");
    View mView = inflater.inflate(R.layout.view_youtube_elements_item, container, false);
    final ImageView imgBlurBackground = (ImageView) mView.findViewById(R.id.imgBlurBackground);
    mImageCallback = new BitmapImageViewTarget(imgBlurBackground) {

      @Override
      public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
        Bitmap resizedbitmap1 = Bitmap.createBitmap(bitmap, 0, 45, 480, 270);
        imgBlurBackground.setImageBitmap(resizedbitmap1);

        boolean isBlack = false;
        int midleImage = bitmap.getHeight() / 2;
        for (int i = 0; i < 20; i++) {
          int pixel = bitmap.getPixel(i, midleImage);
          int r = Color.red(pixel);
          int g = Color.green(pixel);
          int b = Color.blue(pixel);
          if (r == 0 && g == 0 && b == 0) {
            isBlack = true;
          } else {
            isBlack = false;
            break;
          }
        }

        youtubeId = getArguments().getString(EXTRA_YOUTUBE_ID);
        orientation = getArguments().getInt(EXTRA_YOUTUBE_ORIENTARION);

        if (!TextUtils.isEmpty(youtubeId)) {
          if (isBlack || orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setYoutubeFragmentToView(mView, LinearLayout.LayoutParams.MATCH_PARENT, true);
            if (isBlack) {
              YoutubeFragment.this.getActivity()
                  .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
          } else {
            setYoutubeFragmentToView(mView, LinearLayout.LayoutParams.WRAP_CONTENT, false);
          }
        }
      }
    };

    initViews(mView);
    initYoutubeFragment();

    return mView;
  }

  private void initViews(View view) {
    final RelativeLayout youtubeLayoutContainer =
        (RelativeLayout) view.findViewById(R.id.youtubeLayoutContainer);
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

    youtubeId = getArguments().getString(EXTRA_YOUTUBE_ID);
    orientation = getArguments().getInt(EXTRA_YOUTUBE_ORIENTARION);

    String strImgForBlur = "http://img.youtube.com/vi/" + youtubeId + "/hqdefault.jpg";
    Log.e("+++", "initViews " + youtubeId + "orientation" + orientation);
    Glide.with(this)
        .load(strImgForBlur)
        .asBitmap()
        .transform(new BlurTransformation(YoutubeFragment.this.getActivity(), 20))
        .into(mImageCallback);
  }

  private void setYoutubeFragmentToView(View mView, int height, boolean hideOtherViews) {
    FrameLayout layout = (FrameLayout) mView.findViewById(R.id.youtubePlayerFragmentContent);
    ViewGroup.LayoutParams params = layout.getLayoutParams();
    params.height = height; //LinearLayout.LayoutParams.WRAP_CONTENT.;
    params.width = LinearLayout.LayoutParams.WRAP_CONTENT;
    layout.setLayoutParams(params);

    mView.findViewById(R.id.imgBlurBackground)
        .setVisibility(hideOtherViews ? View.GONE : View.VISIBLE);
  }

  private void initYoutubeFragment() {
    try {
      YouTubePlayerFragment2 youTubePlayerFragment2 = YouTubePlayerFragment2.newInstance();
      youTubePlayerFragment2.initialize(BuildConfig.YOUTUBE_DEVELOPER_KEY, onInitializedListener);

      if (this.getActivity() != null && !this.getActivity().isFinishing()) {
        getChildFragmentManager().beginTransaction()
            .replace(R.id.youtubePlayerFragmentContent, youTubePlayerFragment2, YOUTUBE_FRAGMENT)
            .commitAllowingStateLoss();
      }
    } catch (Exception ignored) {
    }
  }

  public void setYouTubePlayer(final YouTubePlayer player) {
    try {
      if (player == null) {
        return;
      }

      player.setFullscreen(false);
      player.setShowFullscreenButton(true);
      player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

      if (playedVideo >= 0) {
        if (playedVideo == 0 || isPlaying) {
          player.loadVideo(youtubeId, playedVideo);
        } else {
          player.cueVideo(youtubeId, playedVideo);
        }
      }
      int screenOrientation = getScreenOrientation();
      player.setFullscreen(screenOrientation == Configuration.ORIENTATION_LANDSCAPE);
    } catch (Exception ignored) {
    }
  }

  public int getScreenOrientation() {
    Display getOrient = getActivity().getWindowManager().getDefaultDisplay();
    int orientation;
    if (getOrient.getWidth() < getOrient.getHeight()) {
      orientation = Configuration.ORIENTATION_PORTRAIT;
    } else {
      orientation = Configuration.ORIENTATION_LANDSCAPE;
    }
    return orientation;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    if (savedInstanceState != null) {
      //Restore the fragment's state here
      playedVideo = savedInstanceState.getInt(EXTRA_PLAYED_VIDEO);
      isPlaying = savedInstanceState.getBoolean(EXTRA_IS_PLAYING);
    }
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    try {
      YouTubePlayerFragment2 youTubePlayerSupportFragment =
          (YouTubePlayerFragment2) getChildFragmentManager().findFragmentByTag(YOUTUBE_FRAGMENT);
      YouTubePlayer mPlayer = youTubePlayerSupportFragment.getPlayer();

      if (mPlayer != null) {
        outState.putInt(EXTRA_PLAYED_VIDEO, mPlayer.getCurrentTimeMillis());
        outState.putBoolean(EXTRA_IS_PLAYING, mPlayer.isPlaying());
        Log.e("+++", "onSaveInstanceState mPlayer != null");
      }
    } catch (Exception ignored) {
    }

    super.onSaveInstanceState(outState);
  }

  @Override public void onResume() {
    super.onResume();
  }

  @Override public void onDestroyView() {
    Log.e("+++", "onDestroy");

    super.onDestroyView();
  }
}
