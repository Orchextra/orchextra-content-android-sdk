package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders;

import android.content.Context;
import android.text.Html;
import android.util.Base64;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import com.gigigo.orchextra.core.domain.entities.article.ArticleHeaderElement;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.core.sdk.utils.ImageGenerator;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;

public class ArticleHeaderView extends ArticleBaseView<ArticleHeaderElement> {

  private ImageLoader imageLoader;

  private ImageView articleHeaderImage;
  private TextView articleHeaderText;

  public ArticleHeaderView(Context context, ArticleHeaderElement articleElement,
      ImageLoader imageLoader) {
    super(context, articleElement);
    this.imageLoader = imageLoader;
  }

  @Override protected void bindTo(final ArticleHeaderElement articleElement) {
    setImage(articleElement.getImageUrl(), articleElement.getImageThumb());

    if (articleElement.getHtml() != null) {
      articleHeaderText.setText(Html.fromHtml(articleElement.getHtml()));
    }
  }

  private void setImage(final String imageUrl, final String imageThumb) {
    articleHeaderImage.getViewTreeObserver()
        .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
          @Override public boolean onPreDraw() {
            byte[] imageThumbBytes = Base64.decode(imageThumb, Base64.DEFAULT);

            String generatedImageUrl = ImageGenerator.generateImageUrl(imageUrl,
                DeviceUtils.calculateRealWidthDevice(getContext()));

            imageLoader.load(generatedImageUrl)
                .thumbnailByte(imageThumbBytes)
                .into(articleHeaderImage);

            articleHeaderImage.getViewTreeObserver().removeOnPreDrawListener(this);

            return true;
          }
        });
  }

  @Override protected void bindViews() {
    articleHeaderImage = (ImageView) itemView.findViewById(R.id.article_header_image);
    articleHeaderText = (TextView) itemView.findViewById(R.id.article_header_text);
  }

  @Override protected int getViewLayout() {
    return R.layout.view_article_header_item;
  }
}
