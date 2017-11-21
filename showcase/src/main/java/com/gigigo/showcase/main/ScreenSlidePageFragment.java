package com.gigigo.showcase.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.orchextra.core.sdk.model.grid.ContentGridLayoutView;
import com.gigigo.orchextra.core.sdk.model.grid.dto.ClipToPadding;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import com.gigigo.showcase.ContentManager;
import com.gigigo.showcase.R;

public class ScreenSlidePageFragment extends Fragment {

  private static final String TAG = "ScreenSlidePageFragment";
  private static final String EXTRA_SCREEN_SLIDE_SECTION = "EXTRA_SCREEN_SLIDE_SECTION";
  private static final String EXTRA_IMAGES_TO_DOWNLOAD = "EXTRA_IMAGES_TO_DOWNLOAD";

  private View emptyView;
  private View errorView;

  public static ScreenSlidePageFragment newInstance(String section, int imagesToDownload) {
    ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();

    Bundle args = new Bundle();
    args.putString(EXTRA_SCREEN_SLIDE_SECTION, section);
    args.putInt(EXTRA_IMAGES_TO_DOWNLOAD, imagesToDownload);
    fragment.setArguments(args);

    return fragment;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);

    emptyView = view.findViewById(R.id.empty_view);
    errorView = view.findViewById(R.id.error_view);

    errorView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ((MainActivity) getActivity()).getContent();
      }
    });
    emptyView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ((MainActivity) getActivity()).getContent();
      }
    });

    return view;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    int imagesToDownload = getArguments().getInt(EXTRA_IMAGES_TO_DOWNLOAD);
    String section = getArguments().getString(EXTRA_SCREEN_SLIDE_SECTION);

    loadContent(section, imagesToDownload);
  }

  private void loadContent(String section, int imagesToDownload) {

    ContentManager contentManager = ContentManager.getInstance();

    contentManager.getContent(section, imagesToDownload,
        new ContentManager.ContentManagerCallback<UiGridBaseContentData>() {
          @Override public void onSuccess(UiGridBaseContentData result) {
            setView(result);
          }

          @Override public void onError(Exception exception) {
            Log.e(TAG, "loadContent", exception);
          }
        });
  }

  public void setView(UiGridBaseContentData contentView) {
    if (contentView != null) {
      contentView.setClipToPaddingBottomSize(ClipToPadding.PADDING_2, 0);
      contentView.setEmptyView(emptyView);
      contentView.setErrorView(errorView);

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
  }
}