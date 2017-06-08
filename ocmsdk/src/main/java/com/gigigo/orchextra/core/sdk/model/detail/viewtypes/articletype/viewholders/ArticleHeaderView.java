package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders;

import android.content.Context;
import android.text.Html;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.gigigo.orchextra.core.domain.entities.article.ArticleHeaderElement;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.core.sdk.utils.ImageGenerator;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;

public class ArticleHeaderView extends ArticleBaseView<ArticleHeaderElement> {

  private final Context context;
  private ImageLoader imageLoader;

  private ImageView articleHeaderImage;
  private TextView articleHeaderText;

  public ArticleHeaderView(Context context, ArticleHeaderElement articleElement,
      ImageLoader imageLoader) {
    super(context, articleElement);

    this.context = context;
    this.imageLoader = imageLoader;
  }

  @Override protected void bindTo(final ArticleHeaderElement articleElement) {
    setImage(articleElement.getImageUrl(), articleElement.getImageThumb());

    if (articleElement.getHtml() != null) {
      articleHeaderText.setText(Html.fromHtml(articleElement.getHtml()));
    }
  }

  private void setImage(final String imageUrl, final String imageThumb) {
    float ratioImage = ImageGenerator.getRatioImage(imageUrl);

    int realWidthDevice = DeviceUtils.calculateRealWidthDevice(getContext());

    if (ratioImage != -1) {
      int calculatedHeight = (int) (realWidthDevice / ratioImage);

      RelativeLayout.LayoutParams lp =
          new RelativeLayout.LayoutParams(realWidthDevice, calculatedHeight);
      articleHeaderImage.setLayoutParams(lp);
    }


    byte[] imageThumbBytes = Base64.decode(imageThumb, Base64.DEFAULT);

    String generatedImageUrl = ImageGenerator.generateImageUrl(imageUrl, realWidthDevice);

    Glide.with(context)
        .load(generatedImageUrl)
        .thumbnail(Glide.with(context).load(imageThumbBytes))
        .priority(Priority.NORMAL)
        .dontAnimate()
        .into(articleHeaderImage);
  }

  @Override protected void bindViews() {
    articleHeaderImage = (ImageView) itemView.findViewById(R.id.article_header_image);
    articleHeaderText = (TextView) itemView.findViewById(R.id.article_header_text);
  }

  @Override protected int getViewLayout() {
    return R.layout.view_article_header_item;
  }
}
