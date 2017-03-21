package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.gigigo.ggglogger.GGGLogImpl;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube.YoutubeContentDataActivity;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube.YoutubeWebviewActivity;
import com.gigigo.orchextra.ocmsdk.BuildConfig;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.orchextra.core.domain.entities.article.ArticleVideoElement;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

public class ArticleVideoView extends ArticleBaseView<ArticleVideoElement> {

  private YouTubeThumbnailView youtubeThumbnail;
  private final Activity activity;

  public ArticleVideoView(Context context, ArticleVideoElement articleElement) {
    super(context, articleElement);
    this.activity = (Activity) context;
  }

  @Override protected int getViewLayout() {
    return R.layout.view_article_video_item;
  }

  @Override protected void bindViews() {
    youtubeThumbnail = (YouTubeThumbnailView) itemView.findViewById(R.id.youtubeThumbnail);
  }

  @Override protected void bindTo(final ArticleVideoElement articleElement) {
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
        //YoutubeWebviewActivity.open(activity, articleElement.getSource());
      //  YoutubeWebviewActivity.open(activity, articleElement.getSource());
        YoutubeContentDataActivity.open(activity, articleElement.getSource());
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
