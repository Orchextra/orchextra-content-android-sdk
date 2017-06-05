package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders;

import android.content.Context;
import android.util.Base64;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageElement;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.core.sdk.utils.ImageGenerator;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;

public class ArticleImageView extends ArticleBaseView<ArticleImageElement> {

  private final Context context;
  private ImageView articleImagePlaceholder;
  private ImageLoader imageLoader;

  public ArticleImageView(Context context, ArticleImageElement articleElement,
      ImageLoader imageLoader) {
    super(context, articleElement);
    this.context = context;
    this.imageLoader = imageLoader;
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

    Glide.with(context)
        .load(generatedImageUrl)
        .thumbnail(Glide.with(context).load(imageThumbBytes))
        .dontAnimate()
        .into(articleImagePlaceholder);
  }
}
