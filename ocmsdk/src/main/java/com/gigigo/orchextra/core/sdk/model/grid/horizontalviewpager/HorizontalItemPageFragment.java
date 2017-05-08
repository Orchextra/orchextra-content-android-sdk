package com.gigigo.orchextra.core.sdk.model.grid.horizontalviewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.gigigo.orchextra.core.controller.dto.CellCarouselContentData;
import com.gigigo.orchextra.ocm.views.UiListedBaseContentData;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;

public class HorizontalItemPageFragment extends Fragment {

  private ImageLoader imageLoader;
  private CellCarouselContentData cell;
  private ImageView horizontalItemImageView;
  private UiListedBaseContentData.ListedContentListener listedContentListener;
  private View.OnClickListener onItemClickListener;

  public static HorizontalItemPageFragment newInstance() {
    return new HorizontalItemPageFragment();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_horizontal_item_pager_view, container, false);

    initViews(view);

    return view;
  }

  private void initViews(View view) {
    horizontalItemImageView = (ImageView) view.findViewById(R.id.horizontalItemImageView);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    setImage();
    setListeners();
  }

  private void setImage() {
    String imageUrl = cell.getData().getSectionView().getImageUrl();
    imageLoader.load(imageUrl).into(horizontalItemImageView);
  }

  private void setListeners() {
    horizontalItemImageView.setOnClickListener(onItemClickListener);
  }

  public void setImageLoader(ImageLoader imageLoader) {
    this.imageLoader = imageLoader;
  }

  public void setCell(CellCarouselContentData cell) {
    this.cell = cell;
  }

  public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }
}
