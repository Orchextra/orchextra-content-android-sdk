package com.gigigo.orchextra.core.sdk.model.detail.layouts;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
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

    if (previewContentData != null && detailContentData != null) {
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
}
