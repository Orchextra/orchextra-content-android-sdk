package com.gigigo.showcase.presentation.view.main.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.orchextra.core.sdk.model.grid.dto.ClipToPadding;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.OcmCallbacks;
import com.gigigo.orchextra.ocm.callbacks.OnLoadMoreContentListener;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import com.gigigo.showcase.R;

public class ScreenSlidePageFragment extends Fragment {

  public static final int ADDICTIONAL_PADDING = 30;

  private UiGridBaseContentData contentView;
  private UiMenu itemMenu;
  private int numberOfImagesToDownload;
  private String emotion;
  private OnLoadMoreContentListener onLoadMoreContentListener;
  private boolean isStopped;
  private View emptyLayoutView;
  private View errorLayoutView;
  private View retryButton;
  private View progressView;
  private EmptyContentCallback emptyContentCallback;

  public static ScreenSlidePageFragment newInstance() {
    return new ScreenSlidePageFragment();
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    setListeners();
    loadSection();
  }

  private void loadSection() {
    if (itemMenu == null) {
      return;
    }

    Ocm.generateSectionView(itemMenu, emotion, numberOfImagesToDownload,
        new OcmCallbacks.Section() {
          @Override public void onSectionLoaded(UiGridBaseContentData uiGridBaseContentData) {
            if (getView() != null) {

              uiGridBaseContentData.setOnLoadMoreContentListener(() -> goToFirstSection());
              setView(uiGridBaseContentData);
              showErrorView(false);
            }
          }

          @Override public void onSectionFails(Exception e) {
            if (getView() != null) {
              showErrorView(true);
            }
          }
        });
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);

    initViews(rootView);

    return rootView;
  }

  private void initViews(View rootView) {
    emptyLayoutView = rootView.findViewById(R.id.empty_view);
    errorLayoutView = rootView.findViewById(R.id.error_view);
    progressView = rootView.findViewById(R.id.woah_progress_bar_grid_layout);
    //retryButton = rootView.findViewById(R.id.retry_button);
  }

  private void setListeners() {
    //retryButton.setOnClickListener(v -> {
    //  if (emptyContentCallback != null) {
    //    emptyContentCallback.onRetryClick();
    //  }
    //});
  }

  public void setView(UiGridBaseContentData contentView) {
    this.contentView = contentView;

    if (contentView != null && isAdded()) {
      int addictionalPaddingInDp =
          (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ADDICTIONAL_PADDING,
              getResources().getDisplayMetrics());
      contentView.setClipToPaddingBottomSize(ClipToPadding.PADDING_5, addictionalPaddingInDp);

      contentView.setEmptyView(emptyLayoutView);
      contentView.setErrorView(errorLayoutView);
      contentView.setProgressView(progressView);

      if (!isStopped) {
        getChildFragmentManager().beginTransaction()
            .replace(R.id.content_main_view, contentView)
            .commit();
      }
    }
  }

  @Override public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    isStopped = true;
  }

  @Override public void onStart() {
    super.onStart();

    isStopped = false;
  }

  @Override public void onStop() {
    super.onStop();

    isStopped = true;
  }

  public void goToFirstSection() {
    if (onLoadMoreContentListener != null) {
      onLoadMoreContentListener.onLoadMoreContent();
    }
  }

  public void showEmptyView(boolean isVisible) {
    if (emptyLayoutView != null) {
      emptyLayoutView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
  }

  public void showErrorView(boolean isVisible) {
    if (emptyLayoutView != null) {
      emptyLayoutView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
  }

  public void updateEmotion(String emotion) {
    if (contentView != null) {
      contentView.setFilter(emotion);
    }
  }

  public void scrollToTop() {
    if (contentView != null) {
      contentView.scrollToTop();
    }
  }

  public void setOnLoadMoreContentListener(OnLoadMoreContentListener onLoadMoreContentListener) {
    this.onLoadMoreContentListener = onLoadMoreContentListener;
  }

  public void reloadSection(boolean hasToShowNewContentButton) {
    if (contentView != null) {
      contentView.reloadSection(false);
    }
  }

  public void setItemMenu(UiMenu itemMenu) {
    this.itemMenu = itemMenu;
  }

  public void setNumberOfImagesToDownload(int numberOfImagesToDownload) {
    this.numberOfImagesToDownload = numberOfImagesToDownload;
  }

  public void setEmotion(String emotion) {
    this.emotion = emotion;
  }

  public void setEmptyContentCallback(EmptyContentCallback emptyContentCallback) {
    this.emptyContentCallback = emptyContentCallback;
  }

  public interface EmptyContentCallback {
    void onRetryClick();
  }
}

