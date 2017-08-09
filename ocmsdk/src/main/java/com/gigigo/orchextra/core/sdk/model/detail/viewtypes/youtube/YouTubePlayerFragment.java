package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube;

import android.os.Bundle;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class YouTubePlayerFragment extends YouTubePlayerSupportFragment {

  private YouTubePlayer mPlayer;

  public static YouTubePlayerFragment newInstance() {
    return new YouTubePlayerFragment();
  }

  @Override public void onCreate(Bundle bundle) {
    super.onCreate(bundle);

    setRetainInstance(true);
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
