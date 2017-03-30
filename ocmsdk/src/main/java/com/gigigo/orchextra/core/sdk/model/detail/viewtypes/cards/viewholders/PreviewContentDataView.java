package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheBehaviour;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCachePreview;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheShare;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.listeners.PreviewFuntionalityListener;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.core.sdk.utils.ImageGenerator;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;

public class PreviewContentDataView extends LinearLayout {

  private final Context context;
  private ElementCachePreview preview;
  private ElementCacheShare share;

  private View previewContentMainLayout;
  private ImageView previewImage;
  private ImageView previewBackgroundShadow;
  private TextView previewTitle;
  private View shareButton;
  private View goToArticleButton;

  private PreviewFuntionalityListener previewFuntionalityListener;
  private ImageLoader imageLoader;

  public PreviewContentDataView(@NonNull Context context) {
    super(context);
    this.context = context;

    init();
  }

  public PreviewContentDataView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    this.context = context;

    init();
  }

  public PreviewContentDataView(@NonNull Context context, @Nullable AttributeSet attrs,
      @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.context = context;

    init();
  }

  private void init() {
    LayoutInflater inflater =
        (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.view_preview_content_data_layout, this, true);

    initViews(view);
  }

  public void setPreview(ElementCachePreview preview) {
    this.preview = preview;
  }

  public void setShare(ElementCacheShare share) {
    this.share = share;
  }

  private void initViews(View view) {
    previewContentMainLayout = view.findViewById(R.id.previewContentMainLayout);
    previewImage = (ImageView) view.findViewById(R.id.preview_image);
    previewBackgroundShadow = (ImageView) view.findViewById(R.id.preview_background);
    previewTitle = (TextView) view.findViewById(R.id.preview_title);
    shareButton = view.findViewById(R.id.share_button);
    goToArticleButton = view.findViewById(R.id.go_to_article_button);
  }

  public void bindTo() {
    if (preview != null) {
      setImage();

      previewTitle.setText(preview.getText());
      if(preview.getText() == null || (preview.getText() != null && preview.getText().isEmpty())) previewBackgroundShadow.setVisibility(View.GONE);

      if (share != null) {
        shareButton.setVisibility(View.VISIBLE);
      }

      if (preview.getBehaviour().equals(ElementCacheBehaviour.SWIPE)) {
        goToArticleButton.setVisibility(View.VISIBLE);
      }

      setAnimations();
    }
  }

  private void setAnimations() {
    Animation animation = AnimationUtils.loadAnimation(context, R.anim.settings_items);
    previewTitle.startAnimation(animation);
    if (shareButton.getVisibility() == View.VISIBLE) {
      shareButton.startAnimation(animation);
    }
  }

  private void setImage() {
    String imageUrl = preview.getImageUrl();

    String generatedImageUrl =
        ImageGenerator.generateImageUrl(imageUrl, DeviceUtils.calculateRealWidthDevice(context),
            DeviceUtils.calculateRealHeightDevice(context));

    imageLoader.load(generatedImageUrl).into(previewImage);
  }

  private void setListeners() {
    shareButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (previewFuntionalityListener != null) {
          previewFuntionalityListener.onClickShare(share);
        }
      }
    });

    if (preview != null && preview.getBehaviour().equals(ElementCacheBehaviour.CLICK)) {
      previewContentMainLayout.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          if (previewFuntionalityListener != null) {
            previewFuntionalityListener.onClickGoToArticleButton();
          }
        }
      });
    }
  }

  public void setPreviewFuntionalityListener(
      PreviewFuntionalityListener previewFuntionalityListener) {
    this.previewFuntionalityListener = previewFuntionalityListener;
  }

  public void hasToDisableSwipeMotion() {
    if (preview != null && previewFuntionalityListener != null && preview.getBehaviour()
        .equals(ElementCacheBehaviour.CLICK)) {
      previewFuntionalityListener.disablePreviewScrolling();
    }
  }

  public void setImageLoader(ImageLoader imageLoader) {
    this.imageLoader = imageLoader;
  }

  public void initialize() {
    bindTo();
    setListeners();
  }
}
