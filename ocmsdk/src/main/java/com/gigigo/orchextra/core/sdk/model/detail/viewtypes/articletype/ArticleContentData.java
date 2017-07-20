package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
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

  @Override public void onDestroy() {

    System.out.println("----onDestroy------------------------------------------artivcle content data");
    if (articleItemViewContainer != null) {
      unbindDrawables(articleItemViewContainer);
      System.gc();

      Glide.get(this.getContext()).clearMemory();
      articleItemViewContainer.removeAllViews();
      Glide.get(this.getContext()).clearMemory();

      articleItemViewContainer = null;
      articleElementList = null;
    }

    super.onDestroy();
  }

  private void unbindDrawables(View view) {
    System.gc();
    Runtime.getRuntime().gc();
    if (view.getBackground() != null) {
      view.getBackground().setCallback(null);
    }
    if (view instanceof ViewGroup) {
      for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
        unbindDrawables(((ViewGroup) view).getChildAt(i));
      }
      ((ViewGroup) view).removeAllViews();
    }
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
