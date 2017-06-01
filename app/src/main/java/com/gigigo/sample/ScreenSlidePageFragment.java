package com.gigigo.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.orchextra.core.sdk.model.grid.dto.ClipToPadding;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;

public class ScreenSlidePageFragment extends Fragment {

  private static final String EXTRA_SCREEN_SLIDE_SECTION = "EXTRA_SCREEN_SLIDE_SECTION";

  private Bundle arguments;
  private View emptyViewLayout;
  private View errorViewLayout;

  public static ScreenSlidePageFragment newInstance(String section) {
    ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();

    Bundle args = new Bundle();
    args.putString(EXTRA_SCREEN_SLIDE_SECTION, section);
    fragment.setArguments(args);

    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    saveArguments();
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    loadArguments();
  }

  private void saveArguments() {
    arguments = getArguments();
  }

  private void loadArguments() {
    if (arguments != null) {
      String section = arguments.getString(EXTRA_SCREEN_SLIDE_SECTION);

      UiGridBaseContentData uiGridBaseContentData = Ocm.generateGridView(section, null);
      setView(uiGridBaseContentData);
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);

    emptyViewLayout = view.findViewById(R.id.emptyViewLayout);
    errorViewLayout = view.findViewById(R.id.errorViewLayout);

    return view;
  }

  public void setView(UiGridBaseContentData contentView) {
    if (contentView != null) {
      contentView.setClipToPaddingBottomSize(ClipToPadding.PADDING_BIG);
      contentView.setEmptyView(emptyViewLayout);
      contentView.setErrorView(errorViewLayout);

      ((com.gigigo.orchextra.core.sdk.model.grid.ContentGridLayoutView) contentView).setViewPagerAutoSlideTime(3000);

      getChildFragmentManager().beginTransaction()
          .replace(R.id.content_main_view, contentView)
          .commit();
    }
  }
}
