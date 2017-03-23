package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.core.domain.entities.article.ArticleElement;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleBaseView;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.ocmsdk.R;

public class CardItemFragment extends UiBaseContentData {

  private ArticleBaseView element;
  private FrameLayout cardItemContainer;

  public static CardItemFragment newInstance() {
    return new CardItemFragment();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_card_item_layout, container, false);

    initViews(view);

    return view;
  }

  private void initViews(View view) {
    cardItemContainer = (FrameLayout) view.findViewById(R.id.cardItemContainer);

    int heightDevice = DeviceUtils.calculateRealHeightDevice(getContext());

    FrameLayout.LayoutParams lp =
        new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightDevice);
    view.setLayoutParams(lp);

    //FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
    //    ViewGroup.LayoutParams.MATCH_PARENT);

    cardItemContainer.removeAllViews();
    cardItemContainer.addView(element, lp);
  }

  public void setElement(ArticleBaseView element) {
    this.element = element;
  }
}
