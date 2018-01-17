package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Priority;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.orchextra.core.data.rxCache.imageCache.loader.OcmImageLoader;
import com.gigigo.orchextra.core.domain.entities.article.ArticleButtonElement;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.core.sdk.utils.ImageGenerator;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.customProperties.ViewCustomizationType;
import com.gigigo.orchextra.ocm.customProperties.ViewType;
import com.gigigo.orchextra.ocmsdk.R;

public class ArticleButtonView extends BaseViewHolder<ArticleButtonElement> {

  private final Context context;
  private TextView articleTextButton;
  private ImageView articleImageButton;
  private boolean isDisabled = false;

  public ArticleButtonView(Context context, ViewGroup parent) {
    super(context, parent, R.layout.view_article_button_item);

    this.context = context;

    articleTextButton = itemView.findViewById(R.id.articleTextButton);
    articleImageButton = itemView.findViewById(R.id.articleImageButton);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void bindTextButton(final ArticleButtonElement articleElement) {
    articleTextButton.setVisibility(View.VISIBLE);

    articleTextButton.setText(articleElement.getRender().getText());

    try {
      articleTextButton.setTextColor(Color.parseColor(articleElement.getRender().getTextColor()));

      if (!isDisabled) {
        articleTextButton.setBackgroundColor(
            Color.parseColor(articleElement.getRender().getBgColor()));
      }
    } catch (Exception ignored) {
    }

    ViewGroup.LayoutParams lp = getLayoutParams(articleElement);
    articleTextButton.setLayoutParams(lp);
  }

  @NonNull private ViewGroup.LayoutParams getLayoutParams(ArticleButtonElement articleElement) {
    int paddingRes = 0;
    switch (articleElement.getRender().getSize()) {
      case BIG:
        paddingRes = R.dimen.ocm_margin_article_big_button;
        break;
      case MEDIUM:
        paddingRes = R.dimen.ocm_margin_article_medium_button;
        break;
      case SMALL:
        paddingRes = R.dimen.ocm_margin_article_small_button;
        break;
    }
    int paddingHeight =
        (int) context.getResources().getDimension(R.dimen.ocm_height_article_button);
    int padding = (int) context.getResources().getDimension(paddingRes);
    FrameLayout.LayoutParams lp =
        new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, paddingHeight);
    lp.setMargins(padding, 0, padding, 0);
    return lp;
  }

  private void bindImageButton(final ArticleButtonElement articleElement) {
    articleImageButton.setVisibility(View.VISIBLE);

    float ratioImage = ImageGenerator.getRatioImage(articleElement.getRender().getImageUrl());

    int realWidthDevice = DeviceUtils.calculateRealWidthDeviceInImmersiveMode(context);
    int calculatedHeight = (int) (realWidthDevice / ratioImage);

    OcmImageLoader.load(context, articleElement.getRender().getImageUrl())
        .priority(Priority.NORMAL)
        .override(realWidthDevice, calculatedHeight)
        .into(articleImageButton);
  }

  @Override public void bindTo(ArticleButtonElement articleButtonElement, int i) {
    ViewCustomizationType[] viewCustomizationTypes = OCManager.getOcmCustomBehaviourDelegate()
        .customizationForContent(articleButtonElement.getCustomProperties(),
            ViewType.BUTTON_ELEMENT);

    for (ViewCustomizationType viewCustomizationType : viewCustomizationTypes) {
      if (viewCustomizationType == ViewCustomizationType.DISABLED) {
        setButtonDisable();
      }
    }

    switch (articleButtonElement.getRender().getType()) {
      case IMAGE:
        bindImageButton(articleButtonElement);
        break;
      case DEFAULT:
        bindTextButton(articleButtonElement);
    }
  }

  @Override public void onClick(View v) {
    if (!isDisabled) {
      super.onClick(v);
    }
  }

  private void setButtonDisable() {
    articleTextButton.setBackgroundColor(
        ContextCompat.getColor(context, R.color.oc_background_detail_view_color));
    isDisabled = true;
  }
}
