package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageElement;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.core.sdk.utils.ImageGenerator;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;

public class CardImageViewHolder extends CardViewElement<ArticleImageElement> {

  private ImageLoader imageLoader;
  private ImageView cardImagePlaceholder;
  private ArticleImageElement imageElement;

  public static CardImageViewHolder newInstance() {
    return new CardImageViewHolder();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.view_card_image_item, container, false);

    initViews(view);

    return view;
  }

  private void initViews(View view) {
    cardImagePlaceholder = (ImageView) view.findViewById(R.id.card_image_placeholder);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    bindTo();
  }

  private void bindTo() {
    if (imageElement != null) {
      setImage(imageElement.getImageUrl(), imageElement.getImageThumb());
    }
  }

  private void setImage(final String imageUrl, String imageThumb) {
    ImageGenerator.generateThumbImage(imageThumb, cardImagePlaceholder);

    int widthDevice = DeviceUtils.calculateRealWidthDevice(getContext());
    //int heightDevice = DeviceUtils.calculateRealHeightDevice(getContext());

    String generatedImageUrl = ImageGenerator.generateImageUrl(imageUrl, widthDevice);

    imageLoader.load(generatedImageUrl).into(cardImagePlaceholder).build();

    //ViewPager.LayoutParams lp = new ViewPager.LayoutParams(widthDevice, heightDevice);
    //cardImagePlaceholder.setLayoutParams(lp);
  }

  public void setImageLoader(ImageLoader imageLoader) {
    this.imageLoader = imageLoader;
  }

  public void setImageElement(ArticleImageElement imageElement) {
    this.imageElement = imageElement;
  }
}
