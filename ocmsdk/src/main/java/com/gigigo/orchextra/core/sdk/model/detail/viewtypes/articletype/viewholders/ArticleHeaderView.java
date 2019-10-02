package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Html;
import android.util.Base64;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.orchextra.core.data.rxCache.imageCache.loader.OcmImageLoader;
import com.gigigo.orchextra.core.domain.entities.article.ArticleHeaderElement;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.core.sdk.utils.ImageGenerator;
import com.gigigo.orchextra.ocmsdk.R;

public class ArticleHeaderView extends BaseViewHolder<ArticleHeaderElement> {

  private final Context context;
  private final boolean thumbnailEnabled;

  private ImageView articleHeaderImage;
  private TextView articleHeaderText;

  public ArticleHeaderView(Context context, ViewGroup parent, boolean thumbnailEnabled) {
    super(context, parent, R.layout.view_article_header_item);

    this.context = context;
    this.thumbnailEnabled = thumbnailEnabled;

    articleHeaderImage = (ImageView) itemView.findViewById(R.id.article_header_image);
    articleHeaderText = (TextView) itemView.findViewById(R.id.article_header_text);
  }

  private void setImage(final String imageUrl, String imageThumb) {
    float ratioImage = ImageGenerator.getRatioImage(imageUrl);

    int realWidthDevice = DeviceUtils.calculateRealWidthDeviceInImmersiveMode(context);

    if (ratioImage != -1) {
      int calculatedHeight = (int) (realWidthDevice / ratioImage);

      LinearLayout.LayoutParams lp =
          new LinearLayout.LayoutParams(realWidthDevice, calculatedHeight);
      articleHeaderImage.setLayoutParams(lp);
    }

    String generatedImageUrl = ImageGenerator.generateImageUrl(imageUrl, realWidthDevice);

    RequestBuilder<Drawable> requestBuilder =
        OcmImageLoader.load(context, generatedImageUrl).priority(Priority.NORMAL).dontAnimate();

    if (thumbnailEnabled && imageThumb != null) {
      byte[] imageThumbBytes = Base64.decode(imageThumb, Base64.DEFAULT);
      requestBuilder = requestBuilder.thumbnail(Glide.with(context).load(imageThumbBytes));
    }

    requestBuilder.into(articleHeaderImage);
  }

  @Override public void bindTo(ArticleHeaderElement articleElement, int i) {
    setImage(articleElement.getRender().getImageUrl(), articleElement.getRender().getImageThumb());

    if (articleElement.getRender().getHtml() != null) {

      Handler handler = new Handler();
      handler.postDelayed(() -> {
        articleHeaderText.setText(Html.fromHtml(articleElement.getRender().getHtml()));
      }, 100);

    }
  }
}
