package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.core.domain.entities.article.ArticleElement;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube.YoutubeWebviewActivity;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;
import java.util.List;

public class ArticleContentData extends UiBaseContentData {

  private List<ArticleElement> articleElementList;
  private ArticleItemViewContainer articleItemViewContainer;
  private ImageLoader imageLoader;

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
    articleItemViewContainer.setImageLoader(imageLoader);
    articleItemViewContainer.addArticleElementList(articleElementList);
  }

  private void initViews(View view) {
    articleItemViewContainer =
        (ArticleItemViewContainer) view.findViewById(R.id.articleItemListLayout);
  }

  public void addItems(List<ArticleElement> articleElementList) {
    this.articleElementList = articleElementList;
  }

  public void setImageLoader(ImageLoader imageLoader) {
    this.imageLoader = imageLoader;
  }
}
