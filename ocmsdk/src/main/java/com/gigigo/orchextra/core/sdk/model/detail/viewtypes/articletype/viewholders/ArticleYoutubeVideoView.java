package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.orchextra.core.data.api.utils.ConnectionUtilsImp;
import com.gigigo.orchextra.core.domain.entities.article.ArticleYoutubeVideoElement;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube.YoutubeContentDataActivity;
import com.gigigo.orchextra.ocmsdk.R;

public class ArticleYoutubeVideoView extends BaseViewHolder<ArticleYoutubeVideoElement> {

  private final Context context;
  private ImageView imgPlay;
  private ImageView imgThumb;
  private final ConnectionUtils connectionUtils;

  public ArticleYoutubeVideoView(Context context, ViewGroup parent) {
    super(context, parent, R.layout.view_article_video_item);

    connectionUtils = new ConnectionUtilsImp(context);

    this.context = context;
    imgPlay = (ImageView) itemView.findViewById(R.id.imgPlay);
    imgThumb = (ImageView) itemView.findViewById(R.id.imgThumb);
  }

  @Override public void bindTo(ArticleYoutubeVideoElement articleElement, int position) {

    View.OnClickListener onYoutubeThumbnailClickListener = v -> {
      if (connectionUtils.hasConnection()) {
        YoutubeContentDataActivity.open(context.getApplicationContext(), articleElement.getRender().getSource());
      } else {
        Snackbar.make(imgThumb, R.string.oc_error_content_not_available_without_internet, Toast.LENGTH_SHORT).show();
      }
    };

    imgPlay.setOnClickListener(onYoutubeThumbnailClickListener);
    imgThumb.setOnClickListener(onYoutubeThumbnailClickListener);

    String youtubeId = articleElement.getRender().getSource();

    String strImgForBlur = "http://img.youtube.com/vi/" + youtubeId + "/hqdefault.jpg";

    Glide.with(context.getApplicationContext()).load(strImgForBlur).listener(new RequestListener<String, GlideDrawable>() {
      @Override public boolean onException(Exception e, String model, Target<GlideDrawable> target,
          boolean isFirstResource) {
        return false;
      }

      @Override public boolean onResourceReady(GlideDrawable resource, String model,
          Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
        imgPlay.setVisibility(View.VISIBLE);
        return false;
      }
    }).into(imgThumb);
  }
}
