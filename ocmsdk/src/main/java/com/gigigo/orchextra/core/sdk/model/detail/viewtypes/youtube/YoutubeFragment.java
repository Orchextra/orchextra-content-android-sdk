package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import com.gigigo.ggglib.device.AndroidSdkVersion;
import com.gigigo.ggglogger.GGGLogImpl;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.ocmsdk.BuildConfig;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;
import com.gigigo.ui.imageloader.ImageLoaderCallback;
import com.gigigo.ui.imageloader.glide.GlideImageLoaderImp;
import com.gigigo.ui.imageloader.glide.transformations.BlurTransformation;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class YoutubeFragment extends UiBaseContentData {

  private static final String EXTRA_YOUTUBE_ID = "EXTRA_YOUTUBE_ID";
  private static final String EXTRA_YOUTUBE_ORIENTARION = "EXTRA_YOUTUBE_ORIENTARION";
  private static final String EXTRA_PLAYED_VIDEO = "EXTRA_PLAYED_VIDEO";

  private int playedVideo;
  private YouTubePlayerSupportFragment youTubePlayerFragment;
  private View mview;
  private String youtubeId;
  private int orientation;
  private OnFullScreenListener onFullScreenModeListener;
  private ImageLoaderCallback mImageCallback;
  private FragmentManager fragmentManager;
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

          if (!wasRestored) {
            setYouTubePlayer(mPlayer);
          }
        }

        @Override public void onInitializationFailure(YouTubePlayer.Provider provider,
            YouTubeInitializationResult error) {
          Log.d("errorMessage:", error.toString());
        }
      };

  public static YoutubeFragment newInstance(String youtubeId, int orientation) {
    YoutubeFragment youtubeElements = new YoutubeFragment();

    Bundle bundle = new Bundle();
    if (youtubeId.equals("eq8ggWSHIgo")) {
      youtubeId = "17uHCHfgs60";//madmax trailer--> "ikO91fQBsTQ";
    }
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
    mImageCallback = new ImageLoaderCallback() {
      @Override public void onSuccess(Bitmap bitmap) {

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
        if (!TextUtils.isEmpty(youtubeId)) {

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

      @Override public void onError(Drawable drawable) {
      }

      @Override public void onLoading() {
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

    ImageLoader glideImageLoaderImp =
        new GlideImageLoaderImp(YoutubeFragment.this.getActivity().getApplicationContext());
    String strImgForBlur = "http://img.youtube.com/vi/" + youtubeId + "/hqdefault.jpg";

    glideImageLoaderImp.load(strImgForBlur)
        .transform(new BlurTransformation(YoutubeFragment.this.getActivity(), 20))
        .into(mImageCallback);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    if (savedInstanceState != null) {
      //Restore the fragment's state here
      playedVideo = savedInstanceState.getInt(EXTRA_PLAYED_VIDEO);
    }

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
  }

  public void setYouTubePlayer(final YouTubePlayer player) {
    player.setOnFullscreenListener(onFullScreenListener);
    player.setFullscreen(false);
    player.setShowFullscreenButton(true);
    player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
    player.loadVideo(youtubeId, playedVideo);
  }

  @Override public void onResume() {
    super.onResume();
  }

  public void setOnFullScreenModeListener(OnFullScreenListener onFullScreenModeListener) {
    this.onFullScreenModeListener = onFullScreenModeListener;
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    if (mPlayer != null) {
      outState.putInt(EXTRA_PLAYED_VIDEO, mPlayer.getCurrentTimeMillis());
    }
  }

  @Override public void onDestroy() {
    if (mPlayer != null) {
      mPlayer.release();
    }
    super.onDestroy();
  }

  public interface OnFullScreenListener {
    void onFullScreen(boolean isFullScreen);
  }
}
