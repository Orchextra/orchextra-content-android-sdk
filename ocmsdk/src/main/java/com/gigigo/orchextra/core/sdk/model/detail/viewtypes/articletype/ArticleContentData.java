package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.bumptech.glide.Glide;
import com.gigigo.baserecycleradapter.adapter.BaseRecyclerAdapter;
import com.gigigo.orchextra.core.domain.entities.article.ArticleButtonElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleHeaderElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleRichTextElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleVimeoVideoElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleYoutubeVideoElement;
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement;
import com.gigigo.orchextra.core.sdk.di.injector.Injector;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleBlankView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleButtonView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleHeaderView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleImageView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleRichTextView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleVimeoVideoView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleYoutubeVideoView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.dto.ArticleBlankElement;
import com.gigigo.orchextra.core.sdk.model.grid.dto.ClipToPadding;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import com.gigigo.orchextra.ocmsdk.R;
import java.util.List;

public class ArticleContentData extends UiGridBaseContentData {

  private List<ArticleElement> articleElementList;
  private RecyclerView articleItemViewContainer;
  private FrameLayout flFA;
  private View faLoading;
  private BaseRecyclerAdapter<ArticleElement> adapter;
  private int addictionalPadding;
  private boolean thumbnailEnabled;

  public static ArticleContentData newInstance() {
    return new ArticleContentData();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Injector injector = OCManager.getInjector();
    if (injector != null) {
      thumbnailEnabled = injector.provideOcmStyleUi().isThumbnailEnabled();
    }
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.view_article_elements_item, container, false);

    initViews(view);
    initRecyclerView();
    setClipToPaddingBottomSize(ClipToPadding.PADDING_NONE, addictionalPadding);

    return view;
  }

  @Override public void onResume() {
    super.onResume();

    flFA.setVisibility(View.INVISIBLE);
    faLoading.setVisibility(View.GONE);
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

  private void initViews(View view) {
    articleItemViewContainer = (RecyclerView) view.findViewById(R.id.articleItemListLayout);

    flFA = (FrameLayout) view.findViewById(R.id.flFA);
    faLoading = (ProgressBar) flFA.findViewById(R.id.progressFA);
  }

  private void initRecyclerView() {
    ArticleContentDataFactory factory = new ArticleContentDataFactory(getContext(), flFA,
        thumbnailEnabled);
    adapter = new BaseRecyclerAdapter(factory);

    adapter.bind(ArticleYoutubeVideoElement.class, ArticleYoutubeVideoView.class);
    adapter.bind(ArticleVimeoVideoElement.class, ArticleVimeoVideoView.class);
    adapter.bind(ArticleRichTextElement.class, ArticleRichTextView.class);
    adapter.bind(ArticleImageElement.class, ArticleImageView.class);
    adapter.bind(ArticleHeaderElement.class, ArticleHeaderView.class);
    adapter.bind(ArticleButtonElement.class, ArticleButtonView.class);
    adapter.bind(ArticleBlankElement.class, ArticleBlankView.class);

    adapter.setMillisIntervalToAvoidDoubleClick(1500);

    articleItemViewContainer.setAdapter(adapter);
    articleItemViewContainer.setLayoutManager(new LinearLayoutManager(getContext()));
    articleItemViewContainer.setNestedScrollingEnabled(false);
    articleItemViewContainer.setHasFixedSize(false);

    if (articleElementList != null) {
      adapter.addAll(articleElementList);
    }
  }

  public void addItems(List<ArticleElement> articleElementList) {
    this.articleElementList = articleElementList;
  }

  @Override public void setFilter(String filter) {

  }

  @Override public void setClipToPaddingBottomSize(ClipToPadding clipToPadding,
      int addictionalPadding) {
    this.addictionalPadding = addictionalPadding;
    if (articleItemViewContainer != null) {
      articleItemViewContainer.setClipToPadding(false);
      articleItemViewContainer.setPadding(0, 0, 0, addictionalPadding);
    }
  }

  @Override public void scrollToTop() {
    if (articleItemViewContainer != null) {
      articleItemViewContainer.scrollTo(0, 0);
    }
  }

  @Override public void setEmptyView(View emptyView) {

  }

  @Override public void setErrorView(View errorLayoutView) {

  }

  @Override public void setProgressView(View progressView) {
    if (progressView != null) {
      faLoading = progressView;
    }
  }

  @Override public void reloadSection(boolean hasToShowNewContentButton) {
    if (adapter != null) {
      adapter.notifyDataSetChanged();
    }
  }
}
