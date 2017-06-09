package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement;
import com.gigigo.orchextra.ocmsdk.R;
import java.util.List;

public class ArticleContentData extends UiBaseContentData {

  private List<ArticleElement> articleElementList;
  private ArticleItemViewContainer articleItemViewContainer;
  private boolean thumbnailEnabled;

  public static ArticleContentData newInstance() {
    return new ArticleContentData();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.view_article_elements_item, container, false);

    initViews(view);

    return view;
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    init();
  }

  private void init() {
    articleItemViewContainer.setThumbnailEnabled(thumbnailEnabled);
    articleItemViewContainer.addArticleElementList(articleElementList);
  }

  private void initViews(View view) {
    articleItemViewContainer =
        (ArticleItemViewContainer) view.findViewById(R.id.articleItemListLayout);
  }

  public void addItems(List<ArticleElement> articleElementList) {
    this.articleElementList = articleElementList;
  }

  public void setThumbnailEnabled(boolean thumbnailEnabled) {
    this.thumbnailEnabled = thumbnailEnabled;
  }
}
