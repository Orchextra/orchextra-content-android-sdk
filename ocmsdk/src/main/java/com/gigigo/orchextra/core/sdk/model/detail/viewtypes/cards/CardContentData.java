package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.core.domain.entities.article.ArticleElement;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;
import java.util.List;

public class CardContentData extends UiBaseContentData {

  private ImageLoader imageLoader;
  private List<ArticleElement> elements;
  private CardItemRecyclerViewContainer cardRecyclerViewContainer;

  public static CardContentData newInstance() {
    return new CardContentData();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.view_cards_elements_item, container, false);

    initViews(view);

    return view;
  }

  private void initViews(View view) {
    cardRecyclerViewContainer =
        (CardItemRecyclerViewContainer) view.findViewById(R.id.cardRecyclerViewContainer);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    init();
  }

  private void init() {
    cardRecyclerViewContainer.setImageLoader(imageLoader);
    cardRecyclerViewContainer.addArticleElementList(elements);
  }

  public void setImageLoader(ImageLoader imageLoader) {
    this.imageLoader = imageLoader;
  }

  public void addItems(List<ArticleElement> elements) {
    this.elements = elements;
  }
}
