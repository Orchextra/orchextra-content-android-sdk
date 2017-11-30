package com.gigigo.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheType;
import com.gigigo.orchextra.core.sdk.model.grid.ContentGridLayoutView;
import com.gigigo.orchextra.core.sdk.model.grid.dto.ClipToPadding;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.OcmCallbacks;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;

public class ScreenSlidePageFragment extends Fragment {

  private static final String EXTRA_SCREEN_SLIDE_SECTION = "EXTRA_SCREEN_SLIDE_SECTION";
  private static final String EXTRA_IMAGES_TO_DOWNLOAD = "EXTRA_IMAGES_TO_DOWNLOAD";

  private Bundle arguments;
  private View emptyViewLayout;
  private View errorViewLayout;
  private UiGridBaseContentData contentView;

  public static ScreenSlidePageFragment newInstance(ElementCache elementCache, int imagesToDownload) {
    ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();

    Bundle args = new Bundle();
    args.putSerializable(EXTRA_SCREEN_SLIDE_SECTION, elementCache);
    args.putInt(EXTRA_IMAGES_TO_DOWNLOAD, imagesToDownload);
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
      ElementCache section = (ElementCache) arguments.getSerializable(EXTRA_SCREEN_SLIDE_SECTION);
      int imagesToDownload = arguments.getInt(EXTRA_IMAGES_TO_DOWNLOAD);

      if (section.getType().equals(ElementCacheType.GO_CONTENT)) {
        generateSectionView(section, imagesToDownload);
      } else {
        generateActionView(section);
      }
    }
  }

  public void generateActionView(ElementCache section) {
    Ocm.generateActionView(section, new OcmCallbacks.Section() {
      @Override public void onSectionLoaded(UiGridBaseContentData uiGridBaseContentData) {
        if (getView() != null) {
          setView(uiGridBaseContentData);
        }
      }

      @Override public void onSectionFails(Exception e) {
        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
      }
    });
  }

  public void generateSectionView(ElementCache section, int imagesToDownload) {
      if (section.getRender() != null) {
        Ocm.generateSectionView(section.getRender().getContentUrl(), null, imagesToDownload, new OcmCallbacks.Section() {
          @Override public void onSectionLoaded(UiGridBaseContentData uiGridBaseContentData) {
            setView(uiGridBaseContentData);
          }

          @Override public void onSectionFails(Exception e) {
            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
          }
        });
      } else {
        Toast.makeText(getActivity(),"Section is null",Toast.LENGTH_LONG).show();
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
  }

  public void reloadSection() {
    if (contentView != null) {
      contentView.reloadSection();
    }
    else
    {
      loadArguments();
      //contentView.reloadSection();
    }
  }
}
