package com.gigigo.orchextra.core.sdk.model.detail.viewtypes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.core.data.rxCache.imageCache.loader.OcmImageLoader;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheBehaviour;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCachePreview;
import com.gigigo.orchextra.core.sdk.di.injector.Injector;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.listeners.PreviewFuntionalityListener;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.core.sdk.utils.ImageGenerator;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.views.MoreContentArrowView;
import com.gigigo.orchextra.ocmsdk.R;

public class PreviewContentData extends UiBaseContentData {

  private Context context;
  private ElementCachePreview preview;

  private View previewContentMainLayout;
  private ImageView previewImage;
  private ImageView previewBackgroundShadow;
  private TextView previewTitle;
  private View goToArticleButton;

  private PreviewFuntionalityListener previewFuntionalityListener;
  private boolean statusBarEnabled;

  public static PreviewContentData newInstance() {
    return new PreviewContentData();
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    this.context = context.getApplicationContext();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.view_preview_item, container, false);

    init(view);
    initDi();

    return view;
  }

  private void initDi() {
    Injector injector = OCManager.getInjector();
    if (injector != null) {
      statusBarEnabled = injector.provideOcmStyleUi().isStatusBarEnabled();
    }
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    bindTo();
    setListeners();
  }

  public void setPreview(ElementCachePreview preview) {
    this.preview = preview;
  }

  private void init(View view) {
    initView(view);
  }

  private void initView(View view) {
    previewContentMainLayout = view.findViewById(R.id.previewContentMainLayout);
    previewImage = (ImageView) view.findViewById(R.id.preview_image);
    previewBackgroundShadow = (ImageView) view.findViewById(R.id.preview_background);
    previewTitle = (TextView) view.findViewById(R.id.preview_title);
    goToArticleButton = view.findViewById(R.id.go_to_article_button);
    MoreContentArrowView imgAnim = (MoreContentArrowView) view.findViewById(R.id.imgMoreContain);
    imgAnim.anim(32, -1);
  }

  public void bindTo() {
    if (preview != null) {
      setImage();
      setBackgroundShadow();

      previewTitle.setText(preview.getText());
      if (preview.getText() == null || (preview.getText() != null && preview.getText().isEmpty())) {
        previewBackgroundShadow.setVisibility(View.GONE);
      }

      if (preview.getBehaviour().equals(ElementCacheBehaviour.SWIPE)) {
        goToArticleButton.setVisibility(View.VISIBLE);
      }

      setAnimations();
    }
  }

  private void setBackgroundShadow() {
    int width, height;
    if (!statusBarEnabled) {
      width = DeviceUtils.calculateRealWidthDeviceInImmersiveMode(context);
      height = DeviceUtils.calculateHeightDeviceInImmersiveMode(context);
    } else {
      width = DeviceUtils.calculateWidthDevice(context);
      height = DeviceUtils.calculateHeightDevice(context);
    }

    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
    previewBackgroundShadow.setLayoutParams(lp);
  }

  private void setAnimations() {
    Animation animation = AnimationUtils.loadAnimation(context, R.anim.oc_settings_items);
    previewTitle.startAnimation(animation);
  }

  private void setImage() {
    String imageUrl = preview.getImageUrl();

    if (imageUrl != null) {
      int width, height;
      if (!statusBarEnabled) {
        width = DeviceUtils.calculateRealWidthDeviceInImmersiveMode(context);
        height = DeviceUtils.calculateHeightDeviceInImmersiveMode(context);
      } else {
        width = DeviceUtils.calculateWidthDevice(context);
        height = DeviceUtils.calculateHeightDevice(context);
        previewImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
      }

      String generatedImageUrl = ImageGenerator.generateImageUrl(imageUrl, width, height);

      Glide.with(this).load(generatedImageUrl).into(previewImage);

      OcmImageLoader.load(this, generatedImageUrl)
          .priority(Priority.NORMAL)
          .diskCacheStrategy(DiskCacheStrategy.ALL)
          .override(width, height)
          .into(previewImage);

      animateAlphaBecauseOfCollapseEnterTransitionImage();
    }
  }

  private void animateAlphaBecauseOfCollapseEnterTransitionImage() {
    previewImage.setAlpha(0f);
    Animation animation = new AlphaAnimation(0.0f, 1.0f);
    animation.setDuration(1000);
    animation.setStartOffset(1000);
    animation.setAnimationListener(new Animation.AnimationListener() {
      @Override public void onAnimationStart(Animation animation) {

      }

      @Override public void onAnimationEnd(Animation animation) {
        previewImage.setAlpha(1f);
      }

      @Override public void onAnimationRepeat(Animation animation) {

      }
    });
    previewImage.startAnimation(animation);
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

  @Override public void onDestroy() {
    if (previewContentMainLayout != null) {
      unbindDrawables(previewContentMainLayout);
      System.gc();

      Glide.get(this.getContext()).clearMemory();
      previewImage = null;
      previewBackgroundShadow = null;
      goToArticleButton = null;
      ((ViewGroup) previewContentMainLayout).removeAllViews();
      Glide.get(this.getContext()).clearMemory();

      previewContentMainLayout = null;
    }

    if (getHost() != null) {
      super.onDestroy();
    }
  }

  private void unbindDrawables(View view) {
    System.gc();
    Runtime.getRuntime().gc();
    if (view.getBackground() != null) {
      view.getBackground().setCallback(null);
    }
    if (view instanceof ViewGroup) {
      for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
        unbindDrawables(((ViewGroup) view).getChildAt(i));
      }
      ((ViewGroup) view).removeAllViews();
    }
  }
}
