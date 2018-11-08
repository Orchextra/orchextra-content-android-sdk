package com.gigigo.orchextra.core.sdk.model.detail;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gigigo.ggglib.device.AndroidSdkVersion;
import com.gigigo.orchextra.core.controller.model.detail.DetailPresenter;
import com.gigigo.orchextra.core.controller.model.detail.DetailView;
import com.gigigo.orchextra.core.data.rxCache.imageCache.loader.OcmImageLoader;
import com.gigigo.orchextra.core.sdk.di.base.BaseInjectionActivity;
import com.gigigo.orchextra.core.sdk.di.injector.Injector;
import com.gigigo.orchextra.core.sdk.utils.ImageGenerator;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.callbacks.OnFinishViewListener;
import com.gigigo.orchextra.ocm.views.UiDetailBaseContentData;
import com.gigigo.orchextra.ocmsdk.R;
import java.util.ArrayList;
import java.util.List;
import orchextra.javax.inject.Inject;
import timber.log.Timber;

public class DetailActivity extends BaseInjectionActivity<DetailActivityComponent>
    implements DetailView {

  private static final String EXTRA_ELEMENT_CACHE = "EXTRA_ELEMENT_CACHE";
  private static final String EXTRA_ELEMENT_URL = "EXTRA_ELEMENT_URL";
  private static final String EXTRA_IMAGE_TO_EXPAND_URL = "EXTRA_IMAGE_TO_EXPAND_URL";
  private static final String EXTRA_WIDTH_IMAGE_TO_EXPAND_URL = "EXTRA_WIDTH_IMAGE_TO_EXPAND_URL";
  private static final String EXTRA_HEIGHT_IMAGE_TO_EXPAND_URL = "EXTRA_HEIGHT_IMAGE_TO_EXPAND_URL";

  @Inject DetailPresenter presenter;
  OnFinishViewListener onFinishViewListener = new OnFinishViewListener() {
    @Override public void onFinish() {
      finishView(isAppbarExpanded());
    }
  };
  private ImageView animationImageView;
  private UiDetailBaseContentData uiContentView;
  private boolean statusBarEnabled;
  private FrameLayout parentContainer;
  @DrawableRes private int detailBackground;

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  public static void open(Activity activity, String elementUrl, String urlImageToExpand, int width,
      int height, final View view) {

    if (activity != null) {
      Intent intent = new Intent(activity, DetailActivity.class);

      intent.putExtra(DetailActivity.EXTRA_ELEMENT_URL, elementUrl);
      intent.putExtra(DetailActivity.EXTRA_IMAGE_TO_EXPAND_URL, urlImageToExpand);
      intent.putExtra(DetailActivity.EXTRA_WIDTH_IMAGE_TO_EXPAND_URL, width);
      intent.putExtra(DetailActivity.EXTRA_HEIGHT_IMAGE_TO_EXPAND_URL, height);

      if (view != null && urlImageToExpand != null) {
        ActivityOptionsCompat optionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, "thumbnail");
        activity.startActivity(intent, optionsCompat.toBundle());
      } else {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.oc_detail_activity_open_out,
            R.anim.oc_detail_activity_open_in);
      }
    } else {
      Timber.e("open() - activity == null");
    }
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail_layout);

    animationImageView = findViewById(R.id.animationImageView);

    try {
      presenter.attachView(this);
    } catch (NullPointerException e) {
      Ocm.logException(e);
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP) @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    if (hasFocus) {
      int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

      if (!statusBarEnabled) {
        flags = flags
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN;

        if (AndroidSdkVersion.hasKitKat19()) {
          flags = flags | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
      } else {
        if (AndroidSdkVersion.hasLollipop21()) {
          getWindow().setStatusBarColor(getResources().getColor(R.color.oc_status_bar_color));
        }
      }

      getWindow().getDecorView().setSystemUiVisibility(flags);
    }
  }

  @Override protected void initDI() {
    Injector injector = OCManager.getInjector();
    if (injector != null) {
      injector.injectDetailActivity(this);
      statusBarEnabled = injector.provideOcmStyleUi().isStatusBarEnabled();
      detailBackground = injector.provideOcmStyleUi().getDetailBackground();
    }
  }

  @Override public void initUi() {
    parentContainer = findViewById(R.id.parentContainer);

    presenter.setOnFinishViewListener(onFinishViewListener);

    setAnimationImageView();

    String elementUrl = getIntent().getStringExtra(EXTRA_ELEMENT_URL);
    presenter.loadSection(elementUrl);

    if (detailBackground != -1) {
      parentContainer.setBackground(ContextCompat.getDrawable(this, detailBackground));
    }
  }

  @Override public void setView(UiDetailBaseContentData uiContentView) {
    this.uiContentView = uiContentView;
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.content_detail_view, uiContentView)
        .commit();
  }

  @Override public void showError() {
    finish();
  }

  @Override public void finishView(boolean showingPreview) {
    finish();
    if (!showingPreview) {
      overridePendingTransition(R.anim.oc_slide_out_right, R.anim.oc_slide_in_left);
    }
  }

  @Override public void onBackPressed() {
    finishView(onFinishViewListener == null || onFinishViewListener.isAppbarExpanded());
  }

  @Override public void setAnimationImageView() {
    String url = getIntent().getStringExtra(EXTRA_IMAGE_TO_EXPAND_URL);
    int width = getIntent().getIntExtra(EXTRA_WIDTH_IMAGE_TO_EXPAND_URL, 0);
    int height = getIntent().getIntExtra(EXTRA_HEIGHT_IMAGE_TO_EXPAND_URL, 0);

    if (!TextUtils.isEmpty(url)) {
      String generateImageUrl = ImageGenerator.generateImageUrl(url, width, height);

      supportPostponeEnterTransition();

      OcmImageLoader.load(this, generateImageUrl)
          .override(width, height)
          .dontAnimate()
          .priority(Priority.HIGH)
          .listener(new RequestListener<Object, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, Object model, Target<GlideDrawable> target,
                boolean isFirstResource) {
              supportStartPostponedEnterTransition();
              return false;
            }

            @Override public boolean onResourceReady(GlideDrawable resource, Object model,
                Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
              supportStartPostponedEnterTransition();
              return false;
            }
          })
          .into(animationImageView);
    }
  }

  @Override protected void onDestroy() {
    unbindDrawables(parentContainer);
    System.gc();
    Glide.get((Context) presenter.getView()).clearMemory();
    if (animationImageView != null) animationImageView = null;

    if (uiContentView != null) {
      uiContentView = null;
    }
    parentContainer.removeAllViews();
    Glide.get((Context) presenter.getView()).clearMemory();
    if (presenter != null) {
      presenter.detachView();
    }

    this.finish();

    super.onDestroy();
  }

  private void unbindDrawables(View view) {
    List<View> viewList = new ArrayList<>();

    viewList.add(view);
    for (int i = 0; i < viewList.size(); i++) {
      View child = viewList.get(i);
      if (child instanceof ViewGroup) {
        ViewGroup viewGroup = (ViewGroup) child;
        for (int j = 0; j < viewGroup.getChildCount(); j++) {
          viewList.add(viewGroup.getChildAt(j));
        }
      }
    }

    for (int i = viewList.size() - 1; i >= 0; i--) {
      View child = viewList.get(i);
      if (child != null && child.getBackground() != null) {
        child.getBackground().setCallback(null);
      }
      if (child instanceof ViewGroup) {
        ((ViewGroup) child).removeAllViews();
      }
    }
  }
}
