package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gigigo.orchextra.core.domain.entities.article.ArticleVideoElement;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube.YoutubeContentDataActivity;
import com.gigigo.orchextra.ocmsdk.R;

public class ArticleVideoView extends ArticleBaseView<ArticleVideoElement> {

  private final Activity activity;

  private ImageView imgPlay;
  private ImageView imgThumb;

  public ArticleVideoView(Context context, ArticleVideoElement articleElement) {
    super(context, articleElement);
    this.activity = (Activity) context;
  }

  @Override protected int getViewLayout() {
    return R.layout.view_article_video_item;
  }

  @Override protected void bindViews() {

    imgPlay = (ImageView) itemView.findViewById(R.id.imgPlay);
    imgThumb = (ImageView) itemView.findViewById(R.id.imgThumb);
  }

  @Override protected void bindTo(final ArticleVideoElement articleElement) {

    View.OnClickListener onYoutubeThumbnailClickListener = new View.OnClickListener() {
      @Override public void onClick(View v) {
        YoutubeContentDataActivity.open(activity, articleElement.getSource());
      }
    };
    imgPlay.setOnClickListener(onYoutubeThumbnailClickListener);
    imgThumb.setOnClickListener(onYoutubeThumbnailClickListener);

    String youtubeId = articleElement.getSource();

    String strImgForBlur = "http://img.youtube.com/vi/" + youtubeId + "/hqdefault.jpg";

    Glide.with(activity).load(strImgForBlur).listener(new RequestListener<String, GlideDrawable>() {
      @Override public boolean onException(Exception e, String model, Target<GlideDrawable> target,
          boolean isFirstResource) {
        return false;
      }

      @Override public boolean onResourceReady(GlideDrawable resource, String model,
          Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
        imgPlay.setVisibility(VISIBLE);
        return false;
      }
    }).into(imgThumb);
  }
}
