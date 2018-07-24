package com.gigigo.orchextra.ocm.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.orchextra.core.sdk.model.grid.ContentGridLayoutView;
import com.gigigo.orchextra.core.sdk.model.grid.dto.ClipToPadding;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.OcmCallbacks;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;

public class ScreenSlidePageFragment extends Fragment {
  private View emptyViewLayout;
  private View errorViewLayout;
  private View newContentView;
  private UiGridBaseContentData contentView;
  private UiMenu itemMenu;
  private int numberOfImagesToDownload;
  private String emotion;

  public static ScreenSlidePageFragment newInstance() {
    return new ScreenSlidePageFragment();
  }

  private void loadContent() {
    Ocm.generateSectionView(itemMenu, emotion, numberOfImagesToDownload,
        new OcmCallbacks.Section() {
          @Override public void onSectionLoaded(UiGridBaseContentData uiGridBaseContentData) {
            setView(uiGridBaseContentData);
          }

          @Override public void onSectionFails(Exception e) {
            e.printStackTrace();
          }
        });
  }

  @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);

    emptyViewLayout = view.findViewById(R.id.emptyViewLayout);
    errorViewLayout = view.findViewById(R.id.errorViewLayout);
    newContentView = view.findViewById(R.id.newContentMainContainer);

    loadContent();

    return view;
  }

  public void setView(UiGridBaseContentData contentView) {
    if (contentView != null) {
      this.contentView = contentView;

      contentView.setClipToPaddingBottomSize(ClipToPadding.PADDING_5, 20);
      contentView.setEmptyView(emptyViewLayout);
      contentView.setErrorView(errorViewLayout);

      if (contentView instanceof ContentGridLayoutView) {
        ((ContentGridLayoutView) contentView).setViewPagerAutoSlideTime(3000);
      }

      FragmentManager childFragmentManager = getChildFragmentManager();

      if (!childFragmentManager.isDestroyed()) {
        childFragmentManager.beginTransaction()
            .replace(R.id.content_main_view, contentView)
            .commit();
      }
    }
    if (itemMenu.hasNewVersion()) {
      showNewVersionButton();
    } else {
      hideNewVersionButton();
    }
  }

  public void reloadSection(boolean hasToShowNewContentButton) {
    if (contentView != null) {
      contentView.reloadSection(hasToShowNewContentButton);
    }
  }

  public void setItemMenu(UiMenu itemMenu) {
    this.itemMenu = itemMenu;
  }

  public void showNewVersionButton() {
    if (newContentView != null) {
      newContentView.setVisibility(View.VISIBLE);
      newContentView.setOnClickListener(v -> {
        newContentView.setVisibility(View.GONE);
        reloadSection(true);
      });
    }
  }

  public void hideNewVersionButton() {
    if (newContentView != null) {
      newContentView.setVisibility(View.GONE);
    }
  }

  public String getSlug() {
    return this.itemMenu.getSlug();
  }

  public void setNumberOfImagesToDownload(int numberOfImagesToDownload) {
    this.numberOfImagesToDownload = numberOfImagesToDownload;
  }
}
