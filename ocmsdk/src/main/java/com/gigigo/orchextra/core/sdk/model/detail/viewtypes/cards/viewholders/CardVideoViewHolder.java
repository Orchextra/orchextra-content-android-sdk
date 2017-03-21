package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.ggglogger.GGGLogImpl;
import com.gigigo.orchextra.core.domain.entities.article.ArticleVideoElement;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube.YoutubeContentDataActivity;
import com.gigigo.orchextra.ocmsdk.BuildConfig;
import com.gigigo.orchextra.ocmsdk.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

public class CardVideoViewHolder extends BaseViewHolder<ArticleVideoElement> {

  private final YouTubeThumbnailView youtubeThumbnail;
  private final Activity activity;

  public CardVideoViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.view_card_video_item);
    this.activity = (Activity) context;

    youtubeThumbnail = (YouTubeThumbnailView) itemView.findViewById(R.id.youtubeThumbnail);
  }

  @Override public void bindTo(final ArticleVideoElement articleElement, int i) {
    YouTubeThumbnailView.OnInitializedListener onInitializedListener =
        new YouTubeThumbnailView.OnInitializedListener() {
          @Override public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView,
              YouTubeThumbnailLoader youTubeThumbnailLoader) {
            youTubeThumbnailLoader.setVideo(articleElement.getSource());
            youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
          }

          @Override public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView,
              YouTubeInitializationResult youTubeInitializationResult) {

            GGGLogImpl.log("youTubeInitializationResult: " + youTubeInitializationResult.toString());
          }
        };

    View.OnClickListener onYoutubeThumbnailClickListener = new View.OnClickListener() {
      @Override public void onClick(View v) {
        YoutubeContentDataActivity.open(activity, articleElement.getSource());
        //YoutubeWebviewActivity.open(activity, articleElement.getSource());
      }
    };

    youtubeThumbnail.initialize(BuildConfig.YOUTUBE_DEVELOPER_KEY, onInitializedListener);
    youtubeThumbnail.setOnClickListener(onYoutubeThumbnailClickListener);
  }

  private YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener =
      new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
        @Override public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView,
            YouTubeThumbnailLoader.ErrorReason errorReason) {

          GGGLogImpl.log("errorReasononThumbnailError: " + errorReason.toString());
        }

        @Override
        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String videoId) {
        }
      };


}
