package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import com.gigigo.orchextra.core.domain.entities.article.ArticleVideoElement;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube.YoutubeContentDataActivity;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;
import com.gigigo.ui.imageloader.ImageLoaderCallback;
import com.gigigo.ui.imageloader.glide.GlideImageLoaderImp;

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
    ImageLoader glideImageLoaderImp = new GlideImageLoaderImp(activity.getApplicationContext());
    String strImgForBlur = "http://img.youtube.com/vi/" + youtubeId + "/hqdefault.jpg";

    glideImageLoaderImp.load(strImgForBlur).into(new ImageLoaderCallback() {
      @Override public void onSuccess(Bitmap bitmap) {
        //this is for prevent show only the play img when the video is down
        imgPlay.setVisibility(VISIBLE);
        imgThumb.setImageBitmap(bitmap);
      }

      @Override public void onError(Drawable drawable) {

      }

      @Override public void onLoading() {

      }
    });
  }
}
