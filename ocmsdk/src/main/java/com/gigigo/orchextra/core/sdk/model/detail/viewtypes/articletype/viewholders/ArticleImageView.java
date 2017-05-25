package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders;

import android.content.Context;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageElement;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.core.sdk.utils.ImageGenerator;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;

public class ArticleImageView extends ArticleBaseView<ArticleImageElement> {

  private ImageView articleImagePlaceholder;
  private ImageLoader imageLoader;

  public ArticleImageView(Context context, ArticleImageElement articleElement,
      ImageLoader imageLoader) {
    super(context, articleElement);
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

  private void setImage(final String imageUrl, String imageThumb) {
    ImageGenerator.generateThumbImage(imageThumb, articleImagePlaceholder);

    articleImagePlaceholder.getViewTreeObserver()
        .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
          @Override public boolean onPreDraw() {

            String generatedImageUrl = ImageGenerator.generateImageUrl(imageUrl,
                DeviceUtils.calculateRealWidthDevice(getContext()),
                DeviceUtils.calculateRealHeightDevice(getContext()));

            imageLoader.load(generatedImageUrl).into(articleImagePlaceholder);

            articleImagePlaceholder.getViewTreeObserver().removeOnPreDrawListener(this);

            return true;
          }
        });
  }
}
