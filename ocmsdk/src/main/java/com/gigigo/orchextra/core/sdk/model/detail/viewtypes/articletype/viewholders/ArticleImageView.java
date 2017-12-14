package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders;

import android.content.Context;
import android.util.Base64;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.orchextra.core.data.rxCache.imageCache.loader.OcmImageLoader;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageElement;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.core.sdk.utils.ImageGenerator;
import com.gigigo.orchextra.ocmsdk.R;

public class ArticleImageView extends BaseViewHolder<ArticleImageElement> {

  private final Context context;
  private ImageView articleImagePlaceholder;
  private boolean thumbnailEnabled;

  public ArticleImageView(Context context, ViewGroup parent, boolean thumbnailEnabled) {
    super(context, parent, R.layout.view_article_image_item);

    this.context = context;
    this.thumbnailEnabled = thumbnailEnabled;

    articleImagePlaceholder = (ImageView) itemView.findViewById(R.id.article_image_placeholder);
  }

  private void setImage(final String imageUrl, final String imageThumb) {
    float ratioImage = ImageGenerator.getRatioImage(imageUrl);

    if (ratioImage != -1) {
      int realWidthDevice = DeviceUtils.calculateRealWidthDeviceInImmersiveMode(context);
      int calculatedHeight = (int) (realWidthDevice / ratioImage);

      FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(realWidthDevice, calculatedHeight);
      articleImagePlaceholder.setLayoutParams(lp);
    }

    String generatedImageUrl = ImageGenerator.generateImageUrl(imageUrl,
        DeviceUtils.calculateRealWidthDeviceInImmersiveMode(context));

    DrawableRequestBuilder<String> requestBuilder =
        OcmImageLoader.load(context, generatedImageUrl).priority(Priority.NORMAL).dontAnimate();

    if (thumbnailEnabled && imageThumb != null) {
      byte[] imageThumbBytes = Base64.decode(imageThumb, Base64.DEFAULT);
      requestBuilder = requestBuilder.thumbnail(Glide.with(context).load(imageThumbBytes));
    }

    requestBuilder.into(articleImagePlaceholder);
  }

  @Override public void bindTo(ArticleImageElement imageElement, int position) {
    setImage(imageElement.getImageUrl(), imageElement.getImageThumb());
  }
}
