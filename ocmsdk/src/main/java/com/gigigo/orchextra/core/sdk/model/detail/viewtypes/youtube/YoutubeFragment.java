package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Toast;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.ocmsdk.BuildConfig;
import com.gigigo.ggglogger.GGGLogImpl;
import com.gigigo.ui.imageloader.ImageLoader;
import com.gigigo.ui.imageloader.ImageLoaderCallback;
import com.gigigo.ui.imageloader.glide.GlideImageLoaderImp;
//import com.gigigo.ui.imageloader.glide.transformations.BlurTransformation;

import com.gigigo.ui.imageloader.glide.transformations.BlurTransformation;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.gigigo.orchextra.ocmsdk.R;

public class YoutubeFragment extends UiBaseContentData {

  private static final String EXTRA_YOUTUBE_ID = "EXTRA_YOUTUBE_ID";

  private Context context;

  private YouTubePlayerSupportFragment youTubePlayerFragment;

  private String youtubeId;
  private OnFullScreenListener onFullScreenModeListener;
  private ImageLoaderCallback mImageCallback = new ImageLoaderCallback() {
    @Override public void onSuccess(Bitmap bitmap) {
      Bitmap bmp = bitmap;//   abmp.getBitmap();

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
      if (!TextUtils.isEmpty(youtubeId)) {
        if (isBlack) {
          //Toast.makeText(YoutubeFragment.this.getActivity(), "ES VERTICAL", Toast.LENGTH_LONG).show();
          setYoutubeFragmentToView(LinearLayout.LayoutParams.MATCH_PARENT);
        } else {
          //Toast.makeText(YoutubeFragment.this.getActivity(), "ES HORIZONTAL", Toast.LENGTH_LONG).show();
          setYoutubeFragmentToView(LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        initYoutubeFragment();
      }
    }

    @Override public void onError(Drawable drawable) {
    }

    @Override public void onLoading() {
    }
  };

  public static YoutubeFragment newInstance(String youtubeId) {
    YoutubeFragment youtubeElements = new YoutubeFragment();

    Bundle bundle = new Bundle();
    if (youtubeId.equals("eq8ggWSHIgo")) youtubeId = "17uHCHfgs60";//madmax trailer--> "ikO91fQBsTQ";
    bundle.putString(EXTRA_YOUTUBE_ID, youtubeId);
    youtubeElements.setArguments(bundle);

    return youtubeElements;
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    this.context = context;
  }

  View mview;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.view_youtube_elements_item, container, false);
    initViews(view);
    mview = view;
    return view;
  }

  private void initViews(View view) {
    final RelativeLayout youtubeLayoutContainer =
        (RelativeLayout) view.findViewById(R.id.youtubeLayoutContainer);
    youtubeLayoutContainer.getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override public void onGlobalLayout() {
            FrameLayout.LayoutParams lp =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);

            //  DeviceUtils.calculateRealHeightDevice(context));

            youtubeLayoutContainer.setLayoutParams(lp);

            youtubeLayoutContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
          }
        });

    youtubeId = getArguments().getString(EXTRA_YOUTUBE_ID);

        ImageLoader glideImageLoaderImp =
        new GlideImageLoaderImp(YoutubeFragment.this.getActivity().getApplicationContext());
    String strImgForBlur = "http://img.youtube.com/vi/" + youtubeId + "/hqdefault.jpg";
    ImageView imgBlurBackground = (ImageView) view.findViewById(R.id.imgBlurBackground);
    glideImageLoaderImp.load(strImgForBlur)
        .into(imgBlurBackground)
        .transform(new BlurTransformation(YoutubeFragment.this.getActivity(), 20)).build();

    glideImageLoaderImp.load(strImgForBlur).loaderCallback(mImageCallback).build();
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    //now in the callback

  }

  private void setYoutubeFragmentToView(int h) {
    youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
    // Gets linearlayout
    FrameLayout layout = (FrameLayout) mview.findViewById(R.id.youtubePlayerFragmentContent);
    // Gets the layout params that will allow you to resize the layout
    ViewGroup.LayoutParams params = layout.getLayoutParams();

    // Changes the height and width to the specified *pixels*
    params.height = h; //LinearLayout.LayoutParams.WRAP_CONTENT.;
    params.width = LinearLayout.LayoutParams.MATCH_PARENT;
    layout.setLayoutParams(params);

    getChildFragmentManager().beginTransaction()
        .replace(R.id.youtubePlayerFragmentContent, youTubePlayerFragment)
        .commit();
  }

  private void initYoutubeFragment() {
    youTubePlayerFragment.initialize(BuildConfig.YOUTUBE_DEVELOPER_KEY, onInitializedListener);
  }

  YouTubePlayer mPlayer;

  public void setYouTubePlayer(YouTubePlayer player) {
    player.setPlayerStateChangeListener(playerStateChangeListener);
    player.setOnFullscreenListener(onFullScreenListener);
    player.setFullscreen(false);
    player.setShowFullscreenButton(true);
    player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
    player.loadVideo(youtubeId);
    mPlayer = player;
  }

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
          Log.d("errorMessage:", error.toString());
        }
      };

  YouTubePlayer.PlayerStateChangeListener playerStateChangeListener =
      new YouTubePlayer.PlayerStateChangeListener() {
        @Override public void onLoading() {
          GGGLogImpl.log("YouTubePlayer onLoading");
        }

        @Override public void onLoaded(String s) {
          GGGLogImpl.log("YouTubePlayer onLoaded: " + s);
        }

        @Override public void onAdStarted() {
          GGGLogImpl.log("YouTubePlayer onAdStarted");
        }

        @Override public void onVideoStarted() {
          GGGLogImpl.log("YouTubePlayer onVideoStarted");
        }

        @Override public void onVideoEnded() {
          GGGLogImpl.log("YouTubePlayer  onVideoEnded");
        }

        @Override public void onError(YouTubePlayer.ErrorReason errorReason) {
          GGGLogImpl.log("YouTubePlayer  onError :" + errorReason.toString());
        }
      };

  private YouTubePlayer.OnFullscreenListener onFullScreenListener =
      new YouTubePlayer.OnFullscreenListener() {
        @Override public void onFullscreen(boolean fullscreen) {
          if (onFullScreenModeListener != null) {
            onFullScreenModeListener.onFullScreen(fullscreen);
          }
        }
      };

  public void setOnFullScreenModeListener(OnFullScreenListener onFullScreenModeListener) {
    this.onFullScreenModeListener = onFullScreenModeListener;
  }

  public interface OnFullScreenListener {
    void onFullScreen(boolean isFullScreen);
  }
}
