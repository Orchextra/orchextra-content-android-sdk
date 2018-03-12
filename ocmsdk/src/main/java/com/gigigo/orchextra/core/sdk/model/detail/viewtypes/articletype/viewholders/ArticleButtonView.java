package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Priority;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.orchextra.core.data.rxCache.imageCache.loader.OcmImageLoader;
import com.gigigo.orchextra.core.domain.entities.article.ArticleButtonElement;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.core.sdk.utils.ImageGenerator;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.customProperties.Disabled;
import com.gigigo.orchextra.ocm.customProperties.ViewCustomizationType;
import com.gigigo.orchextra.ocm.customProperties.ViewType;
import com.gigigo.orchextra.ocmsdk.R;

public class ArticleButtonView extends BaseViewHolder<ArticleButtonElement> {

  private static final String TAG = "ArticleButtonView";
  private final Context context;
  private TextView articleTextButton;
  private ImageView articleImageButton;
  private ProgressBar progress;
  private boolean isDisabled = false;
  private boolean isLoading = false;

  public ArticleButtonView(Context context, ViewGroup parent) {
    super(context, parent, R.layout.view_article_button_item);

    this.context = context;

    articleTextButton = itemView.findViewById(R.id.articleTextButton);
    articleImageButton = itemView.findViewById(R.id.articleImageButton);
    progress = itemView.findViewById(R.id.notification_progress);
  }

  private void bindTextButton(@NonNull final ArticleButtonElement articleElement) {
    articleTextButton.setVisibility(View.VISIBLE);

    if (articleElement.getRender() == null) {
      return;
    }

    if (!isLoading) {
      articleTextButton.setText(articleElement.getRender().getText());
    } else {
      if (articleElement.getRender() != null && articleElement.getRender().getBgColor() != null) {

        String bgColor = articleElement.getRender().getBgColor();
        if (bgColor != null) {
          articleTextButton.setBackgroundColor(Color.parseColor(bgColor.replace("#", "#1A")));
        }
      }
    }

    String textColor = articleElement.getRender().getTextColor();
    if (textColor != null) {
      articleTextButton.setTextColor(Color.parseColor(textColor));
    }

    if (!isLoading && !isDisabled) {
      articleTextButton.setBackgroundColor(
          Color.parseColor(articleElement.getRender().getBgColor()));
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

    Handler handler = new Handler();
    handler.postDelayed(() -> {
      switch (articleButtonElement.getRender().getType()) {
        case IMAGE:
          bindImageButton(articleButtonElement);
          break;
        case DEFAULT:
          bindTextButton(articleButtonElement);
      }
    }, 100);

    if (articleButtonElement.getCustomProperties() != null) {
      showLoading();

      OCManager.notifyCustomizationForContent(articleButtonElement.getCustomProperties(),
          ViewType.BUTTON_ELEMENT, customizations -> {
            setButtonEnabled(articleButtonElement);
            for (ViewCustomizationType viewCustomizationType : customizations) {
              if (viewCustomizationType instanceof Disabled) {
                setButtonDisable(articleButtonElement);
              }
            }
            hideLoading(articleButtonElement);
            return null;
          });
    } else {
      hideLoading(articleButtonElement);
    }
  }

  @Override public void onClick(View v) {
    if (!isDisabled && !isLoading) {
      super.onClick(v);
    }
  }

  private void setButtonDisable(@NonNull ArticleButtonElement articleElement) {
    if (articleElement.getRender() != null) {

      String bgColor = articleElement.getRender().getBgColor();
      if (bgColor != null) {
        articleTextButton.setBackgroundColor(Color.parseColor(bgColor.replace("#", "#4D")));
      }

      articleImageButton.setColorFilter(0xBBFFFFFF, PorterDuff.Mode.MULTIPLY);
      isDisabled = true;
    }
  }

  private void setButtonEnabled(@NonNull ArticleButtonElement articleElement) {
    if (articleElement.getRender() != null && articleElement.getRender().getBgColor() != null) {
      articleTextButton.setBackgroundColor(
          Color.parseColor(articleElement.getRender().getBgColor()));
    }
    articleImageButton.clearColorFilter();
    isDisabled = false;
  }

  private void showLoading() {
    progress.setVisibility(View.VISIBLE);
    articleTextButton.setText("");
    isLoading = true;
  }

  private void hideLoading(@NonNull ArticleButtonElement articleElement) {
    progress.setVisibility(View.GONE);
    if (articleElement.getRender() != null) {
      articleTextButton.setText(articleElement.getRender().getText());
    }
    isLoading = false;
  }
}
