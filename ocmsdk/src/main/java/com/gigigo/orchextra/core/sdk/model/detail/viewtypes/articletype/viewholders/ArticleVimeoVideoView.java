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
import com.gigigo.orchextra.core.domain.entities.article.ArticleVimeoVideoElement;
import com.gigigo.orchextra.core.domain.rxInteractor.GetVideo;
import com.gigigo.orchextra.core.domain.rxInteractor.PriorityScheduler;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
import com.gigigo.orchextra.ocm.views.MoreContentArrowView;
import com.gigigo.orchextra.ocmsdk.R;
import gigigo.com.vimeolibs.VideoObserver;
import gigigo.com.vimeolibs.VimeoCallback;
import gigigo.com.vimeolibs.VimeoExoPlayerActivity;
import gigigo.com.vimeolibs.VimeoInfo;

public class ArticleVimeoVideoView extends BaseViewHolder<ArticleVimeoVideoElement> {

  private final Context context;
  private final ConnectionUtils connectionUtils;
  private GetVideo getVideo;
  private ImageView imgPlay;
  private ImageView imgThumb;
  private VimeoInfo mVimeoInfo;

  public ArticleVimeoVideoView(Context context, ViewGroup parent) {
    super(context, parent, R.layout.view_article_video_item_vimeo);

    connectionUtils = new ConnectionUtilsImp(context);

    this.context = context;
    imgPlay = (ImageView) itemView.findViewById(R.id.imgPlay);
    imgThumb = (ImageView) itemView.findViewById(R.id.imgThumb);
  }

  @Override public void bindTo(ArticleVimeoVideoElement articleElement, int position) {
    //todo truchingvimeo
    View.OnClickListener onVimeoThumbnailClickListener = v -> {
      if (connectionUtils.hasConnection()) {
        if (mVimeoInfo != null) VimeoExoPlayerActivity.open(context, mVimeoInfo);
      } else {
        Snackbar.make(imgThumb, R.string.oc_error_content_not_available_without_internet,
            Toast.LENGTH_SHORT).show();
      }
    };

    getVideo.execute(new VideoObserver(new VimeoCallback() {

          @Override public void onSuccess(VimeoInfo vimeoInfo) {
            mVimeoInfo = vimeoInfo;
            String strImgForBlur = mVimeoInfo.getThumbPath();

            imgThumb.setMaxHeight((int) MoreContentArrowView.convertDpToPixel(192,context.getApplicationContext()));
            imgThumb.setScaleType(ImageView.ScaleType.CENTER_CROP);

            Glide.with(context.getApplicationContext())

                .load(strImgForBlur)
                //.asBitmap()
                // .transform(new BlurTransformation(context, 20))
                .listener(new RequestListener<String, GlideDrawable>() {
                  @Override
                  public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                      boolean isFirstResource) {
                    return false;
                  }

                  @Override public boolean onResourceReady(GlideDrawable resource, String model,
                      Target<GlideDrawable> target, boolean isFromMemoryCache,
                      boolean isFirstResource) {
                    imgPlay.setVisibility(View.VISIBLE);
                    return false;
                  }
                })

                .into(imgThumb);
            imgPlay.setOnClickListener(onVimeoThumbnailClickListener);
            imgThumb.setOnClickListener(onVimeoThumbnailClickListener);
          }

          @Override public void onError(Exception e) {
            System.out.println("Error VimeoCallbacak" + e.toString());
          }
        }), GetVideo.Params.Companion.forVideo(context, false, articleElement.getRender().getSource(),
        connectionUtils.isConnectedWifi(), connectionUtils.isConnectedMobile()),
        PriorityScheduler.Priority.HIGH);
  }
}
