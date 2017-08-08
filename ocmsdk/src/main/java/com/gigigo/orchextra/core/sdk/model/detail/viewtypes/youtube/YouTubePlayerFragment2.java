package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class YouTubePlayerFragment2 extends YouTubePlayerSupportFragment {

  private YouTubePlayer mPlayer;

  public static YouTubePlayerFragment2 newInstance() {
    return new YouTubePlayerFragment2();
  }

  @Override
  public void initialize(String s, YouTubePlayer.OnInitializedListener onInitializedListener) {
    super.initialize(s, new YouTubePlayer.OnInitializedListener() {
      @Override public void onInitializationSuccess(YouTubePlayer.Provider provider,
          YouTubePlayer youTubePlayer, boolean b) {

        mPlayer = youTubePlayer;

        onInitializedListener.onInitializationSuccess(provider, youTubePlayer, b);
      }

      @Override public void onInitializationFailure(YouTubePlayer.Provider provider,
          YouTubeInitializationResult youTubeInitializationResult) {

        onInitializedListener.onInitializationFailure(provider, youTubeInitializationResult);
      }
    });
  }

  @Override public void onDestroyView() {
    if (mPlayer != null) {

      mPlayer.release();
    }
    super.onDestroyView();
  }

  public YouTubePlayer getPlayer() {
    return mPlayer;
  }
}
