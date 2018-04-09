package com.gigigo.orchextra.core.sdk.model.grid.verticalviewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.gigigo.orchextra.core.controller.dto.CellCarouselContentData;
import com.gigigo.orchextra.core.data.rxCache.imageCache.loader.OcmImageLoader;
import com.gigigo.orchextra.ocmsdk.R;

public class VerticalItemPageFragment extends Fragment {

  private CellCarouselContentData cell;
  private ImageView verticalItemImageView;
  private OnItemClick onClickVerticalItem;
  private View verticalItemContainer;

  public static VerticalItemPageFragment newInstance() {
    return new VerticalItemPageFragment();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_vertical_item_pager_view, container, false);

    initViews(view);

    return view;
  }

  private void initViews(View view) {
    verticalItemContainer = view.findViewById(R.id.verticalItemContainer);
    verticalItemImageView = view.findViewById(R.id.verticalItemImageView);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    setImage();
    setListeners();
  }

  private void setImage() {
    if (cell != null) {

      String imageUrl = cell.getData().getSectionView().getImageUrl();

      OcmImageLoader.load(this, imageUrl).into(verticalItemImageView);
    }
  }

  private void setListeners() {
    verticalItemImageView.setOnClickListener(v -> {
      if (onClickVerticalItem != null) {
        onClickVerticalItem.onClickItem(verticalItemContainer);
      }
    });
  }

  public void setCell(CellCarouselContentData cell) {
    this.cell = cell;
  }

  public void setOnClickItem(OnItemClick onClickHorizontalItem) {
    this.onClickVerticalItem = onClickHorizontalItem;
  }

  public interface OnItemClick {
    void onClickItem(View view);
  }
}
