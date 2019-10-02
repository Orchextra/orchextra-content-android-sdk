package com.gigigo.orchextra.core.sdk.model.detail.layouts;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import com.bumptech.glide.Glide;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheShare;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.BrowserContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.DeepLinkContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.PreviewContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.ScanContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.VuforiaContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.WebViewContentData;
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
  private NestedScrollView nestedScrollView;
  private boolean isFirstTime = true;
  private boolean isAppBarExpanded = true;
  private static boolean isKeyboardPreviuslyOpened = false;

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
                  if (!(detailContentData instanceof BrowserContentData)
                      && !(detailContentData instanceof WebViewContentData)) {
                    detailToolbarView.switchBetweenButtonAndToolbar(true, true, true);
                  } else {
                    detailToolbarView.switchBetweenButtonAndToolbar(false, true, true);
                  }
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
    nestedScrollView = (NestedScrollView) view.findViewById(R.id.nestedScrollView);
    //region hidesoftkeyboard, for when hide keyboard scroll to top
    //todo only add this code when webview is the detail
    coordinatorLayout.getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override public void onGlobalLayout() {
            Rect r = new Rect();
            //r will be populated with the coordinates of your view that area still visible.
            coordinatorLayout.getWindowVisibleDisplayFrame(r);

            int heightDiff = coordinatorLayout.getRootView().getHeight() - (r.bottom - r.top);
            if (heightDiff > 200) { //   probably a keyboard...fucking android SO
              System.out.println("TECLAO OPEN");
              isKeyboardPreviuslyOpened = true;
            } else {
              System.out.println("TECLAO CLOSE");
              if (isKeyboardPreviuslyOpened) {
                isKeyboardPreviuslyOpened = false;
                nestedScrollView.post(new Runnable() {
                  @Override public void run() {
                    nestedScrollView.startNestedScroll(View.SCROLL_AXIS_VERTICAL);
                    nestedScrollView.stopNestedScroll();
                    nestedScrollView.getChildAt(0).scrollTo(0,0);
                  }
                });
              }
            }
          }
        });
    //endregion
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
    closeView();
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

    if (previewContentData != null) {
      previewContentData.onDestroy();
    }
    if (detailContentData != null) {
      detailContentData.onDestroy();
    }

    Glide.get(this.getContext()).clearMemory();

    coordinatorLayout = null;
    appbarLayout = null;
    collapsingToolbar = null;
    previewContentData = null;
    detailContentData = null;
    Glide.get(this.getContext()).clearMemory();

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
      if (child.getBackground() != null) {
        child.getBackground().setCallback(null);
      }
      if (child instanceof ViewGroup) {
        ((ViewGroup) child).removeAllViews();
      }
    }
  }
}
