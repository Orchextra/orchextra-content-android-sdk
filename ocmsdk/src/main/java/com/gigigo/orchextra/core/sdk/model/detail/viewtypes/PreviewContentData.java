package com.gigigo.orchextra.core.sdk.model.detail.viewtypes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheBehaviour;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCachePreview;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheShare;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.listeners.PreviewFuntionalityListener;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.core.sdk.utils.ImageGenerator;
import com.gigigo.orchextra.ocm.views.MoreContentArrowView;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;

public class PreviewContentData extends UiBaseContentData {

  private Context context;
  private ElementCachePreview preview;
  private ElementCacheShare share;

  private View previewContentMainLayout;
  private ImageView previewImage;
  private ImageView previewBackgroundShadow;
  private TextView previewTitle;
  private View goToArticleButton;

  private PreviewFuntionalityListener previewFuntionalityListener;
  private ImageLoader imageLoader;

  public static PreviewContentData newInstance() {
    return new PreviewContentData();
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);

    this.context = context;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.view_preview_item, container, false);

    init(view);

    return view;
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    bindTo();
    setListeners();
  }

  public void setPreview(ElementCachePreview preview) {
    this.preview = preview;
  }

  public void setShare(ElementCacheShare share) {
    this.share = share;
  }

  private void init(View view) {
    initView(view);

    previewContentMainLayout.getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override public void onGlobalLayout() {
            CollapsingToolbarLayout.LayoutParams lp = new CollapsingToolbarLayout.LayoutParams(
                CollapsingToolbarLayout.LayoutParams.MATCH_PARENT,
                DeviceUtils.calculateRealHeightDevice(context));
            previewContentMainLayout.setLayoutParams(lp);
          }
        });
  }

  private void initView(View view) {
    previewContentMainLayout = view.findViewById(R.id.previewContentMainLayout);
    previewImage = (ImageView) view.findViewById(R.id.preview_image);
    previewBackgroundShadow = (ImageView) view.findViewById(R.id.preview_background);
    previewTitle = (TextView) view.findViewById(R.id.preview_title);
    goToArticleButton = view.findViewById(R.id.go_to_article_button);
    MoreContentArrowView imgAnim  = (MoreContentArrowView) view.findViewById(R.id.imgMoreContain);
    imgAnim.Anim(32,-1);
  }

  private void bindTo() {
    if (preview != null) {
      setImage();

      previewTitle.setText(preview.getText());
      if(preview.getText() == null || (preview.getText() != null && preview.getText().isEmpty())) previewBackgroundShadow.setVisibility(View.GONE);

      if (preview.getBehaviour().equals(ElementCacheBehaviour.SWIPE)) {
        goToArticleButton.setVisibility(View.VISIBLE);
      }

      setAnimations();
    }
  }

  private void setAnimations() {
    Animation animation = AnimationUtils.loadAnimation(context, R.anim.settings_items);
    previewTitle.startAnimation(animation);
  }

  private void setImage() {
    String imageUrl = preview.getImageUrl();

    String generatedImageUrl =
        ImageGenerator.generateImageUrl(imageUrl, DeviceUtils.calculateRealWidthDevice(context),
            DeviceUtils.calculateRealHeightDevice(context));

    imageLoader.load(generatedImageUrl).into(previewImage) ;
  }

  private void setListeners() {
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
}
