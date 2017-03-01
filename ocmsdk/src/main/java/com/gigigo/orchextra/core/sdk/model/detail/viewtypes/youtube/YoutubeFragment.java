package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.ocmsdk.BuildConfig;
import com.gigigo.ggglogger.GGGLogImpl;
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

  public static YoutubeFragment newInstance(String youtubeId) {
    YoutubeFragment youtubeElements = new YoutubeFragment();

    Bundle bundle = new Bundle();
    bundle.putString(EXTRA_YOUTUBE_ID, youtubeId);
    youtubeElements.setArguments(bundle);

    return youtubeElements;
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);

    this.context = context;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.view_youtube_elements_item, container, false);

    initViews(view);

    return view;
  }

  private void initViews(View view) {
    final RelativeLayout youtubeLayoutContainer =
        (RelativeLayout) view.findViewById(R.id.youtubeLayoutContainer);
    youtubeLayoutContainer.getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override public void onGlobalLayout() {
            FrameLayout.LayoutParams lp =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    DeviceUtils.calculateRealHeightDevice(context));

            youtubeLayoutContainer.setLayoutParams(lp);

            youtubeLayoutContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
          }
        });
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    youtubeId = getArguments().getString(EXTRA_YOUTUBE_ID);
    if (!TextUtils.isEmpty(youtubeId)) {
      setYoutubeFragmentToView();
      initYoutubeFragment();
    }
  }

  private void setYoutubeFragmentToView() {
    youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();

    getChildFragmentManager().beginTransaction()
        .replace(R.id.youtubePlayerFragmentContent, youTubePlayerFragment)
        .commit();
  }

  private void initYoutubeFragment() {
    youTubePlayerFragment.initialize(BuildConfig.YOUTUBE_DEVELOPER_KEY,
        onInitializedListener);
  }

  public void setYouTubePlayer(YouTubePlayer player) {
    player.setPlayerStateChangeListener(playerStateChangeListener);
    player.setOnFullscreenListener(onFullScreenListener);
    player.setFullscreen(true);
    player.setShowFullscreenButton(false);
    player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
    player.loadVideo(youtubeId);
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

  private YouTubePlayer.OnFullscreenListener onFullScreenListener = new YouTubePlayer.OnFullscreenListener() {
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
