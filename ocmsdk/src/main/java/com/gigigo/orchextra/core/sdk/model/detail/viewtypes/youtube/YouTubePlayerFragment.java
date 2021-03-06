package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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

  @Override public void onSaveInstanceState(Bundle bundle) {
    super.onSaveInstanceState(bundle);

    /**
     * https://stackoverflow.com/questions/44379747/youtube-android-player-api-throws-badparcelableexception-classnotfoundexception
     * Disable view state saving to prevent saving states from youtube apk which cannot be restored.
     * This solution does not remove YouTube player state from bundle so youtube player will be
     * restored successfully.
     */
    View view = getView();
    if (view instanceof ViewGroup) {
      ViewGroup viewGroup = ((ViewGroup) view);
      for (int i = 0; i < viewGroup.getChildCount(); i++) {
        viewGroup.getChildAt(i).setSaveFromParentEnabled(false);
      }
    }
  }

  public YouTubePlayer getPlayer() {
    return mPlayer;
  }
}
