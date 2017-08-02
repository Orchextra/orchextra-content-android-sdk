package com.gigigo.orchextra.core.sdk.model.detail.layouts;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.bumptech.glide.Glide;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheShare;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.BrowserContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.DeepLinkContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.PreviewContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.ScanContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.VuforiaContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.listeners.PreviewFuntionalityListener;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube.YoutubeContentData;
import com.gigigo.orchextra.ocmsdk.R;
import java.util.ArrayList;
import java.util.List;

public class DetailCoordinatorLayoutContentData extends DetailParentContentData {

  private FrameLayout collapsingToolbar;
  private AppBarLayout appbarLayout;
  private CoordinatorLayout coordinatorLayout;
  private UiBaseContentData previewContentData;
  private UiBaseContentData detailContentData;
  private boolean isFirstTime = true;
  private boolean isAppBarExpanded = true;

  private PreviewFuntionalityListener previewFuntionalityListener =
      new PreviewFuntionalityListener() {

        @Override public void onClickShare(ElementCacheShare share) {
          if (onShareListener != null) {
            onShareListener.onShare();
          }
        }

        @Override public void onClickGoToArticleButton() {
          if (appbarLayout != null) {
            new Handler().postDelayed(new Runnable() {
              @Override public void run() {
                if (!checkIfOxActionAndExecute(detailContentData)) {
                  coordinatorLayout.removeView(appbarLayout);
                  detailToolbarView.switchBetweenButtonAndToolbar(true, true);
                } else {
                  closeView();
                }
              }
            }, 200);
          }
        }

        @Override public void disablePreviewScrolling() {
          if (collapsingToolbar != null) {
            AppBarLayout.LayoutParams params =
                (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
            params.setScrollFlags(0);
            detailToolbarView.blockSwipeEvents(true);
          }
        }
      };

  public static DetailCoordinatorLayoutContentData newInstance() {
    return new DetailCoordinatorLayoutContentData();
  }

  protected void initViews(View view) {
    collapsingToolbar = (FrameLayout) view.findViewById(R.id.collapsingToolbar);
    appbarLayout = (AppBarLayout) view.findViewById(R.id.appbarLayout);
    coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
  }

  @Override protected int getDetailLayout() {
    return R.layout.view_detail_elements_coordinator_layout;
  }

  @Override protected void closeView() {
    if (onFinishListener != null) {
      onFinishListener.setAppbarExpanded(isAppBarExpanded);
      onFinishListener.onFinish();
    }
  }

  public void setViews(UiBaseContentData previewContentData, UiBaseContentData detailContentData) {
    this.previewContentData = previewContentData;
    this.detailContentData = detailContentData;
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    if (previewContentData != null && detailContentData != null && isAdded()) {
      ((AppCompatActivity) getContext()).getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.previewDetailContentLayout, previewContentData)
          .replace(R.id.viewDetailContentLayout, detailContentData)
          .commit();

      setListenerToPreview();
      setListenerWhenScroll();
    }
  }

  private void setListenerToPreview() {
    if (previewContentData != null && previewContentData instanceof PreviewContentData) {
      PreviewContentData previewLayoutView = ((PreviewContentData) previewContentData);

      previewLayoutView.setPreviewFuntionalityListener(previewFuntionalityListener);

      previewLayoutView.hasToDisableSwipeMotion();
    }
  }

  public void setListenerWhenScroll() {
    appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

      @Override public void onOffsetChanged(final AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
          isAppBarExpanded = true;
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()
            && isAppBarExpanded) {
          isAppBarExpanded = false;
          if (detailContentData instanceof VuforiaContentData
              || detailContentData instanceof ScanContentData
              || detailContentData instanceof BrowserContentData
              || detailContentData instanceof YoutubeContentData
              || detailContentData instanceof DeepLinkContentData) {
            executeExternalAction();
          }
        }
        if (onFinishListener != null) {
          onFinishListener.setAppbarExpanded(isAppBarExpanded);
        }
      }
    });
  }

  private void executeExternalAction() {
    checkIfOxActionAndExecute(detailContentData);

    new Handler().postDelayed(new Runnable() {
      @Override public void run() {
        appbarLayout.setExpanded(true, false);
      }
    }, 1000);

    if (detailContentData instanceof DeepLinkContentData) {
      closeView();
    }
  }

  @Override public void onResume() {
    super.onResume();

    if (!isFirstTime) {
      appbarLayout.setExpanded(false, false);
    }
    isFirstTime = false;
  }

  @Override public void onDestroy() {

    System.out.println(
        "----onDestroy------------------------------------------artivcle coordinator content data");
    //if (collapsingToolbar != null) unbindDrawables(collapsingToolbar);
    //if (appbarLayout != null) unbindDrawables(appbarLayout);
    if (coordinatorLayout != null) unbindDrawables(coordinatorLayout);
    System.gc();

    Glide.get(this.getContext()).clearMemory();
    coordinatorLayout.removeAllViews();
    appbarLayout.removeAllViews();
    collapsingToolbar.removeAllViews();
    previewContentData.onDestroy();
    detailContentData.onDestroy();

    Glide.get(this.getContext()).clearMemory();

    coordinatorLayout = null;
    appbarLayout = null;
    collapsingToolbar = null;
    previewContentData = null;
    detailContentData = null;
    Glide.get(this.getContext()).clearMemory();

    super.onDestroy();
  }

  private int count;
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
      if (child.getBackground() != null) {
        child.getBackground().setCallback(null);
      }
      if (child instanceof ViewGroup) {
        ((ViewGroup) child).removeAllViews();
      }
    }

    //if (view.getBackground() != null) {
    //  view.getBackground().setCallback(null);
    //}
    //if (view instanceof ViewGroup) {
    //  for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
    //    unbindDrawables(((ViewGroup) view).getChildAt(i));
    //  }
    //  ((ViewGroup) view).removeAllViews();
    //}
    //Log.i("TAG", "Count Coordinator: " + count++);
  }
}
