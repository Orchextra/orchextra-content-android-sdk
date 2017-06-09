package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders;

import android.content.Context;
import android.util.Base64;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageElement;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.core.sdk.utils.ImageGenerator;
import com.gigigo.orchextra.ocmsdk.R;

public class ArticleImageView extends ArticleBaseView<ArticleImageElement> {

  private final Context context;
  private ImageView articleImagePlaceholder;
  private boolean thumbnailEnabled;

  public ArticleImageView(Context context, ArticleImageElement articleElement,
      boolean thumbnailEnabled) {
    super(context, articleElement);
    this.context = context;
    this.thumbnailEnabled = thumbnailEnabled;
  }

  @Override protected int getViewLayout() {
    return R.layout.view_article_image_item;
  }

  @Override protected void bindViews() {
    articleImagePlaceholder = (ImageView) itemView.findViewById(R.id.article_image_placeholder);
  }

  @Override protected void bindTo(ArticleImageElement articleElement) {
    setImage(articleElement.getImageUrl(), articleElement.getImageThumb());
  }

  private void setImage(final String imageUrl, final String imageThumb) {
    float ratioImage = ImageGenerator.getRatioImage(imageUrl);

    if (ratioImage != -1) {
      int realWidthDevice = DeviceUtils.calculateRealWidthDevice(getContext());
      int calculatedHeight = (int) (realWidthDevice / ratioImage);

      FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(realWidthDevice, calculatedHeight);
      articleImagePlaceholder.setLayoutParams(lp);
    }

    byte[] imageThumbBytes = Base64.decode(imageThumb, Base64.DEFAULT);

    String generatedImageUrl = ImageGenerator.generateImageUrl(imageUrl,
        DeviceUtils.calculateRealWidthDevice(getContext()));

    DrawableRequestBuilder<String> requestBuilder =
        Glide.with(context).load(generatedImageUrl).priority(Priority.NORMAL).dontAnimate();

    if (thumbnailEnabled) {
      requestBuilder = requestBuilder.thumbnail(Glide.with(context).load(imageThumbBytes));
    }

    requestBuilder.into(articleImagePlaceholder);
  }
}
