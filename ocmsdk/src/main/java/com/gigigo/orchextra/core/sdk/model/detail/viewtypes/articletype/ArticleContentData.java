package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.gigigo.baserecycleradapter.adapter.BaseRecyclerAdapter;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.core.domain.entities.article.ArticleButtonElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleHeaderElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleRichTextElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleVideoElement;
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleBlankView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleButtonView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleHeaderView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleImageView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleRichTextView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleVideoView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.dto.ArticleBlankElement;
import com.gigigo.orchextra.ocmsdk.R;
import java.util.List;

public class ArticleContentData extends UiBaseContentData {

  private List<ArticleElement> articleElementList;
  private RecyclerView articleItemViewContainer;
  private BaseRecyclerAdapter adapter;

  public static ArticleContentData newInstance() {
    return new ArticleContentData();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.view_article_elements_item, container, false);

    initViews(view);
    initRecyclerView();

    return view;
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    init();
  }

  @Override public void onDestroy() {
    System.out.println(
        "----onDestroy------------------------------------------artivcle content data");
    if (articleItemViewContainer != null) {
      unbindDrawables(articleItemViewContainer);
      System.gc();

      Glide.get(this.getContext()).clearMemory();
      articleItemViewContainer.removeAllViews();
      Glide.get(this.getContext()).clearMemory();

      articleItemViewContainer = null;
      articleElementList = null;
    }

    if (getHost() != null) {
      super.onDestroy();
    }
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
    //articleItemViewContainer.setThumbnailEnabled(thumbnailEnabled);
    //articleItemViewContainer.addArticleElementList(articleElementList);
  }

  private void initViews(View view) {
    articleItemViewContainer =
        (RecyclerView) view.findViewById(R.id.articleItemListLayout);
  }

  private void initRecyclerView() {
    ArticleContentDataFactory factory = new ArticleContentDataFactory(getContext());
    adapter = new BaseRecyclerAdapter(factory);

    adapter.bind(ArticleVideoElement.class, ArticleVideoView.class);
    adapter.bind(ArticleRichTextElement.class, ArticleRichTextView.class);
    adapter.bind(ArticleImageElement.class, ArticleImageView.class);
    adapter.bind(ArticleHeaderElement.class, ArticleHeaderView.class);
    adapter.bind(ArticleButtonElement.class, ArticleButtonView.class);
    adapter.bind(ArticleBlankElement.class, ArticleBlankView.class);

    adapter.setMillisIntervalToAvoidDoubleClick(1500);

    //adapter.setItemClickListener(
    //    (position, view) -> Toast.makeText(getContext(), "Pulsado: "+ position, Toast.LENGTH_SHORT).show());

    articleItemViewContainer.setAdapter(adapter);
    articleItemViewContainer.setLayoutManager(new LinearLayoutManager(getContext()));
    articleItemViewContainer.setNestedScrollingEnabled(false);
    articleItemViewContainer.setHasFixedSize(false);

    if (adapter != null) {
      adapter.addAll(articleElementList);
    }
  }

  public void addItems(List<ArticleElement> articleElementList) {
    this.articleElementList = articleElementList;
  }
}
