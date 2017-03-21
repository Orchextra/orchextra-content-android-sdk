package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageElement;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.core.sdk.utils.ImageGenerator;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;

public class CardImageViewHolder extends BaseViewHolder<ArticleImageElement> {

  private final Context context;
  private final ImageLoader imageLoader;
  private final ImageView cardImagePlaceholder;

  public CardImageViewHolder(Context context, ViewGroup parent, ImageLoader imageLoader) {
    super(context, parent, R.layout.view_card_image_item);
    this.context = context;
    this.imageLoader = imageLoader;

    cardImagePlaceholder = (ImageView) itemView.findViewById(R.id.card_image_placeholder);
  }

  private void setImage(final String imageUrl, String imageThumb) {
    ImageGenerator.generateThumbImage(imageThumb, cardImagePlaceholder);

    int widthDevice = DeviceUtils.calculateRealWidthDevice(context);
    int heightDevice = DeviceUtils.calculateRealHeightDevice(context);

    String generatedImageUrl =
        ImageGenerator.generateImageUrl(imageUrl, widthDevice);

    imageLoader.load(generatedImageUrl).into(cardImagePlaceholder).build();

    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(widthDevice, heightDevice);
    cardImagePlaceholder.setLayoutParams(lp);
  }

  @Override public void bindTo(ArticleImageElement imageElement, int i) {
    setImage(imageElement.getImageUrl(), imageElement.getImageThumb());
  }
}
