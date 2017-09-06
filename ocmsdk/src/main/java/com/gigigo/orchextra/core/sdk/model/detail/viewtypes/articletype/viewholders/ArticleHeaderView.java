package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders;

import android.content.Context;
import android.text.Html;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.gigigo.orchextra.core.data.rxCache.imageCache.loader.OcmImageLoader;
import com.gigigo.orchextra.core.domain.entities.article.ArticleHeaderElement;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.core.sdk.utils.ImageGenerator;
import com.gigigo.orchextra.ocmsdk.R;

public class ArticleHeaderView extends ArticleBaseView<ArticleHeaderElement> {

  private final Context context;
  private final boolean thumbnailEnabled;

  private ImageView articleHeaderImage;

  private TextView articleHeaderText;

  public ArticleHeaderView(Context context, ArticleHeaderElement articleElement,
      boolean thumbnailEnabled) {
    super(context, articleElement);

    this.context = context;
    this.thumbnailEnabled = thumbnailEnabled;
  }

  @Override protected void bindTo(final ArticleHeaderElement articleElement) {
    setImage(articleElement.getImageUrl(), articleElement.getImageThumb());

    if (articleElement.getHtml() != null) {
      articleHeaderText.setText(Html.fromHtml(articleElement.getHtml()));
    }
  }

  private void setImage(final String imageUrl, final String imageThumb) {
    float ratioImage = ImageGenerator.getRatioImage(imageUrl);

    int realWidthDevice = DeviceUtils.calculateRealWidthDeviceInImmersiveMode(getContext());

    if (ratioImage != -1) {
      int calculatedHeight = (int) (realWidthDevice / ratioImage);

      LinearLayout.LayoutParams lp =
          new LinearLayout.LayoutParams(realWidthDevice, calculatedHeight);
      articleHeaderImage.setLayoutParams(lp);
    }

    byte[] imageThumbBytes = Base64.decode(imageThumb, Base64.DEFAULT);

    String generatedImageUrl = ImageGenerator.generateImageUrl(imageUrl, realWidthDevice);

    DrawableRequestBuilder<String> requestBuilder =
        OcmImageLoader.load(context, generatedImageUrl).priority(Priority.NORMAL).dontAnimate();

    if (thumbnailEnabled) {
      requestBuilder = requestBuilder.thumbnail(Glide.with(context).load(imageThumbBytes));
    }

    requestBuilder.into(articleHeaderImage);
  }

  @Override protected void bindViews() {
    articleHeaderImage = (ImageView) itemView.findViewById(R.id.article_header_image);
    articleHeaderText = (TextView) itemView.findViewById(R.id.article_header_text);
  }

  @Override protected int getViewLayout() {
    return R.layout.view_article_header_item;
  }
}
