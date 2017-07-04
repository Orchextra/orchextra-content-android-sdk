package com.gigigo.orchextra.core.sdk.model.grid.horizontalviewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.gigigo.orchextra.core.controller.dto.CellCarouselContentData;
import com.gigigo.orchextra.core.data.rxCache.imageCache.loader.OcmImageLoader;
import com.gigigo.orchextra.ocmsdk.R;

public class HorizontalItemPageFragment extends Fragment {

  private CellCarouselContentData cell;
  private ImageView horizontalItemImageView;
  private OnClickHorizontalItem onClickHorizontalItem;
  private View horizontalItemContainer;

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
    horizontalItemContainer = view.findViewById(R.id.horizontalItemContainer);
    horizontalItemImageView = (ImageView) view.findViewById(R.id.horizontalItemImageView);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    setImage();
    setListeners();
  }

  private void setImage() {
    if (cell != null) {

      String imageUrl = cell.getData().getSectionView().getImageUrl();

      OcmImageLoader.load(this, imageUrl).into(horizontalItemImageView);
    }
  }

  private void setListeners() {
    horizontalItemImageView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (onClickHorizontalItem != null) {
          onClickHorizontalItem.onClickItem(horizontalItemContainer);
        }
      }
    });
  }

  public void setCell(CellCarouselContentData cell) {
    this.cell = cell;
  }

  public void setOnClickHorizontalItem(OnClickHorizontalItem onClickHorizontalItem) {
    this.onClickHorizontalItem = onClickHorizontalItem;
  }

  public interface OnClickHorizontalItem {
    void onClickItem(View view);
  }
}
