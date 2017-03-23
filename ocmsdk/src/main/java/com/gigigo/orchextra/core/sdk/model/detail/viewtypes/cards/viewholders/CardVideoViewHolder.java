package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.ggglogger.GGGLogImpl;
import com.gigigo.orchextra.core.domain.entities.article.ArticleVideoElement;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube.YoutubeContentDataActivity;
import com.gigigo.orchextra.ocmsdk.BuildConfig;
import com.gigigo.orchextra.ocmsdk.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

public class CardVideoViewHolder extends CardViewElement<ArticleVideoElement> {

  private YouTubeThumbnailView youtubeThumbnail;
  private ArticleVideoElement articleElement;

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

  public static CardVideoViewHolder newInstance() {
    return new CardVideoViewHolder();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.view_card_video_item, container, false);

    initViews(view);

    return view;
  }

  private void initViews(View view) {
    youtubeThumbnail = (YouTubeThumbnailView) view.findViewById(R.id.youtubeThumbnail);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    if (articleElement != null) {
      bindTo();
    }
  }

  private void bindTo() {
    YouTubeThumbnailView.OnInitializedListener onInitializedListener =
        new YouTubeThumbnailView.OnInitializedListener() {
          @Override public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView,
              YouTubeThumbnailLoader youTubeThumbnailLoader) {
            youTubeThumbnailLoader.setVideo(articleElement.getSource());
            youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
          }

          @Override public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView,
              YouTubeInitializationResult youTubeInitializationResult) {

            GGGLogImpl.log(
                "youTubeInitializationResult: " + youTubeInitializationResult.toString());
          }
        };

    View.OnClickListener onYoutubeThumbnailClickListener = new View.OnClickListener() {
      @Override public void onClick(View v) {
        YoutubeContentDataActivity.open(getActivity(), articleElement.getSource());
        //YoutubeWebviewActivity.open(activity, articleElement.getSource());
      }
    };

    youtubeThumbnail.initialize(BuildConfig.YOUTUBE_DEVELOPER_KEY, onInitializedListener);
    youtubeThumbnail.setOnClickListener(onYoutubeThumbnailClickListener);
  }

  public void setArticleElement(ArticleVideoElement articleElement) {
    this.articleElement = articleElement;
  }
}
