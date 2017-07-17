package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class YoutubeFragment extends UiBaseContentData {

  private static final String EXTRA_YOUTUBE_ID = "EXTRA_YOUTUBE_ID";
  private static final String EXTRA_YOUTUBE_ORIENTARION = "EXTRA_YOUTUBE_ORIENTARION";
  private static final String EXTRA_PLAYED_VIDEO = "EXTRA_PLAYED_VIDEO";
  private static final String EXTRA_IS_PLAYING = "EXTRA_IS_PLAYING";

  private int playedVideo;
  private boolean isPlaying;
  private YouTubePlayerSupportFragment youTubePlayerFragment;
  private View mview;
  private String youtubeId;
  private int orientation;
  private OnFullScreenListener onFullScreenModeListener;
  private FragmentManager fragmentManager;
  private BitmapImageViewTarget mImageCallback;

  private YouTubePlayer mPlayer;

  private YouTubePlayer.OnFullscreenListener onFullScreenListener =
      new YouTubePlayer.OnFullscreenListener() {
        @Override public void onFullscreen(boolean fullscreen) {
          if (onFullScreenModeListener != null) {
            onFullScreenModeListener.onFullScreen(fullscreen);
          }
        }
      };
  YouTubePlayer.OnInitializedListener onInitializedListener =
      new YouTubePlayer.OnInitializedListener() {

        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
            boolean wasRestored) {
          mPlayer = player;
          Log.e("+++", "+++++++++++++++++\n\n\n\n\n\n\n onInitializationSuccess +++++++++++++++"
              + "++++++++++++++"
              + "provider"
              + provider.toString()
              + " wasRestored"
              + wasRestored);
          if (!wasRestored) {
            setYouTubePlayer(mPlayer);
          }
        }

        @Override public void onInitializationFailure(YouTubePlayer.Provider provider,
            YouTubeInitializationResult error) {
          //asv quizas solucione  mPlayer = null;
          Log.e("+++:","onInitializationFailure" + error.toString());
        }
      };

  public static YoutubeFragment newInstance(String youtubeId, int orientation) {
    YoutubeFragment youtubeElements = new YoutubeFragment();

    Log.e("+++", "YoutubeFragment newInstance" + youtubeId + "orientation" + orientation);

    Bundle bundle = new Bundle();
    //if (youtubeId.equals("eq8ggWSHIgo")) {
    //  youtubeId = "17uHCHfgs60";//madmax trailer--> "ikO91fQBsTQ";
    //}
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
    View view = inflater.inflate(R.layout.view_youtube_elements_item, container, false);
    mview = view;
    youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
    fragmentManager = getChildFragmentManager();
    final ImageView imgBlurBackground = (ImageView) view.findViewById(R.id.imgBlurBackground);
    mImageCallback = new BitmapImageViewTarget(imgBlurBackground) {

      @Override
      public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
        Bitmap resizedbitmap1 = Bitmap.createBitmap(bitmap, 0, 45, 480, 270);
        imgBlurBackground.setImageBitmap(resizedbitmap1);
        Bitmap bmp = bitmap;

        boolean isBlack = false;
        int midleImage = bmp.getHeight() / 2;
        for (int i = 0; i < 20; i++) {
          int pixel = bmp.getPixel(i, midleImage);
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
        Log.e("+++", "onResourceReady " + youtubeId + "orientation" + orientation);

        if (!TextUtils.isEmpty(youtubeId)) {
          Log.e("+++", "TextUtils.isEmpty(youtubeId) " + youtubeId + "orientation" + orientation);
          if (isBlack || orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Toast.makeText(YoutubeFragment.this.getActivity(), "ES VERTICAL", Toast.LENGTH_LONG).show();
            setYoutubeFragmentToView(LinearLayout.LayoutParams.MATCH_PARENT);
            if (isBlack) {
              YoutubeFragment.this.getActivity()
                  .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
          } else {
            //Toast.makeText(YoutubeFragment.this.getActivity(), "ES HORIZONTAL", Toast.LENGTH_LONG).show();
            setYoutubeFragmentToView(LinearLayout.LayoutParams.WRAP_CONTENT);
          }
        }
      }
    };
    initViews(view);

    return view;
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

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    if (savedInstanceState != null) {
      //Restore the fragment's state here
      playedVideo = savedInstanceState.getInt(EXTRA_PLAYED_VIDEO);
      isPlaying = savedInstanceState.getBoolean(EXTRA_IS_PLAYING);
      Log.e("+++",
          "onActivityCreated savedInstanceState !=null" + youtubeId + "orientation" + orientation);
    }
    Log.e("+++", "onActivityCreated playedVideo" + playedVideo + "isplayed" + isPlaying);
    initYoutubeFragment();
  }

  private void setYoutubeFragmentToView(int h) {

    // Gets linearlayout
    FrameLayout layout = (FrameLayout) mview.findViewById(R.id.youtubePlayerFragmentContent);
    // Gets the layout params that will allow you to resize the layout
    ViewGroup.LayoutParams params = layout.getLayoutParams();
    Log.e("***", "************************\n\n\n\n\n\n\n PASO************************");

    params.height = h; //LinearLayout.LayoutParams.WRAP_CONTENT.;
    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
    layout.setLayoutParams(params);
    if (this.getActivity() != null && !this.getActivity().isFinishing()) {
      fragmentManager.beginTransaction()
          .replace(R.id.youtubePlayerFragmentContent, youTubePlayerFragment)
          .commitAllowingStateLoss();
    }
  }

  private void initYoutubeFragment() {
    youTubePlayerFragment.initialize(BuildConfig.YOUTUBE_DEVELOPER_KEY, onInitializedListener);
    Log.e("+++",
        "+++++++++++++++++++++++++++++\n\n\n\n\n\n\n initYoutubeFragment +++++++++++++++++++++++++++++");
  }

  public void setYouTubePlayer(final YouTubePlayer player) {

    try{
      if (player != null) {
        Log.e("+++", "\n \n\n\n\n\n\n\n\n\n setYouTubePlayer player != null \n\n\n\n\n\n\n\n" + player);
        Log.e("+++", "\n \n\n\n\n\n\n\n\n\n setYouTubePlayer \n\n\n\n\n\n\n\n" );
      } else {
        Log.e("+++", "\n \n\n\n\n\n\n\n\n\n setYouTubePlayer PLAYER NULL \n\n\n\n\n\n\n\n");
      }
      player.setOnFullscreenListener(onFullScreenListener);
      Log.e("+++", "**** 1");
      player.setFullscreen(false);
      Log.e("+++", "**** 2");
      player.setShowFullscreenButton(true);
      Log.e("+++", "**** 3");
      player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
      Log.e("+++", "**** 4");
      if (playedVideo == 0 || isPlaying) {
        try {
          Log.e("+++", "\n \n\n\n\n\n\n\n\n\n try to load \n\n\n\n\n\n\n\n");
          player.loadVideo(youtubeId, playedVideo);
        } catch (Throwable tr) {
          Log.e("+++", "\n \n\n\n\n\n\n\n\n\n YUUUPI CAPTURADO PETE EN loadVideon \n\n\n\n\n\n\n\n"
              + tr.toString());
        }
      } else {
        try {
          Log.e("+++", "\n \n\n\n\n\n\n\n\n\n try to cueVideo sin playedvideo\n\n\n\n\n\n\n\n" + youtubeId +"played"+playedVideo);
          player.cueVideo(youtubeId);
          //Log.e("+++", "\n \n\n\n\n\n\n\n\n\n try to cueVideo  con played \n\n\n\n\n\n\n\n" + youtubeId +"played"+playedVideo);
          //player.cueVideo(youtubeId, playedVideo);
        } catch (IllegalArgumentException e)
        {
          Log.e("+++", "\n \n\n\n\n\n\n\n\n\n YUUUPI CAPTURADO PETE EN IllegalArgumentException cueVideon \n\n\n\n\n\n\n\n"
              + e.toString());

        }
        catch (Throwable tr) {
          Log.e("+++", "\n \n\n\n\n\n\n\n\n\n YUUUPI CAPTURADO PETE EN cueVideon \n\n\n\n\n\n\n\n"
              + tr.toString());
        }
      }}
    catch (Throwable tr) {
      Log.e("+++", "\n \n\n\n\n\n\n\n\n\n YUUUPI CAPTURADO PETE EN setYouTubePlayer antes de load y todo \n\n\n\n\n\n\n\n"
          + tr.toString());
    }
  }

  @Override public void onResume() {
    super.onResume();
  }

  public void setOnFullScreenModeListener(OnFullScreenListener onFullScreenModeListener) {
    this.onFullScreenModeListener = onFullScreenModeListener;
  }

  @Override public void onSaveInstanceState(Bundle outState) {

    if (mPlayer != null) {
      outState.putInt(EXTRA_PLAYED_VIDEO, mPlayer.getCurrentTimeMillis());
      outState.putBoolean(EXTRA_IS_PLAYING, mPlayer.isPlaying());
      Log.e("+++", "onSaveInstanceState mPlayer != null");
    }

    super.onSaveInstanceState(outState);
    Log.e("+++", "onSaveInstanceState");
  }

  @Override public void onDestroy() {
    Log.e("+++", "onDestroy");
    if (mPlayer != null) {
      mPlayer.release();
      Log.e("+++", "onDestroy mPlayer ");
    }
    super.onDestroy();
  }

  public interface OnFullScreenListener {
    void onFullScreen(boolean isFullScreen);
  }
}
