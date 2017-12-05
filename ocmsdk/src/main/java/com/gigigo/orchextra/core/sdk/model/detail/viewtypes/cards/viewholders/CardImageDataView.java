package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Priority;
import com.gigigo.orchextra.core.data.rxCache.imageCache.loader.OcmImageLoader;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageElement;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.core.sdk.utils.ImageGenerator;
import com.gigigo.orchextra.ocmsdk.R;

public class CardImageDataView extends CardDataView {

  private ImageView cardImagePlaceholder;
  private ArticleImageElement imageElement;

  public CardImageDataView(@NonNull Context context) {
    super(context);

    init();
  }

  public CardImageDataView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);

    init();
  }

  public CardImageDataView(@NonNull Context context, @Nullable AttributeSet attrs,
      @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    init();
  }

  @Override public void initialize() {
    bindTo();
  }

  private void init() {
    LayoutInflater inflater =
        (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.view_card_image_item, this, true);

    initViews(view);
  }

  private void initViews(View view) {
    cardImagePlaceholder = (ImageView) view.findViewById(R.id.card_image_placeholder);
  }

  private void bindTo() {
    if (imageElement != null) {
      setImage(imageElement.getImageUrl(), imageElement.getImageThumb());
    }
  }

  private void setImage(final String imageUrl, String imageThumb) {
    ImageGenerator.generateThumbImage(imageThumb, cardImagePlaceholder);

    int widthDevice = DeviceUtils.calculateRealWidthDeviceInImmersiveMode(getContext());

    String generatedImageUrl = ImageGenerator.generateImageUrl(imageUrl, widthDevice);

    OcmImageLoader.load(getContext(), generatedImageUrl)
        .priority(Priority.NORMAL)
        .into(cardImagePlaceholder);
  }

  public void setImageElement(ArticleImageElement imageElement) {
    this.imageElement = imageElement;
  }
}
