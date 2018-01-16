package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import com.gigigo.ggglogger.GGGLogImpl;
import com.gigigo.orchextra.core.domain.entities.article.ArticleYoutubeVideoElement;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube.YoutubeContentDataActivity;
import com.gigigo.orchextra.ocmsdk.BuildConfig;
import com.gigigo.orchextra.ocmsdk.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

public class CardVideoView extends CardDataView {

  private final Context context;

  private YouTubeThumbnailView youtubeThumbnail;
  private ArticleYoutubeVideoElement articleElement;

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

  public CardVideoView(@NonNull Context context) {
    super(context);
    this.context = context;

    init();
  }

  public CardVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    this.context = context;

    init();
  }

  public CardVideoView(@NonNull Context context, @Nullable AttributeSet attrs,
      @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.context = context;

    init();
  }

  private void init() {
    LayoutInflater inflater =
        (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.view_card_video_item, this, true);

    initViews(view);
  }

  private void initViews(View view) {
    youtubeThumbnail = (YouTubeThumbnailView) view.findViewById(R.id.youtubeThumbnail);
  }

  private void bindTo() {
    YouTubeThumbnailView.OnInitializedListener onInitializedListener =
        new YouTubeThumbnailView.OnInitializedListener() {
          @Override public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView,
              YouTubeThumbnailLoader youTubeThumbnailLoader) {
            youTubeThumbnailLoader.setVideo(articleElement.getRender().getSource());
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
        YoutubeContentDataActivity.open(context.getApplicationContext(), articleElement.getRender().getSource());
        //YoutubeWebviewActivity.open(activity, articleElement.getSource());
      }
    };

    youtubeThumbnail.initialize(BuildConfig.YOUTUBE_DEVELOPER_KEY, onInitializedListener);
    youtubeThumbnail.setOnClickListener(onYoutubeThumbnailClickListener);
  }

  public void setArticleElement(ArticleYoutubeVideoElement articleElement) {
    this.articleElement = articleElement;
  }

  @Override public void initialize() {
    if (articleElement != null) {
      bindTo();
    }
  }
}
