package com.gigigo.orchextra.core.sdk.model.grid.articles;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.orchextra.core.controller.model.home.articles.ArticleView;
import com.gigigo.orchextra.core.controller.model.home.articles.ContentArticleHomeLayoutViewPresenter;
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement;
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElementRender;
import com.gigigo.orchextra.core.sdk.di.injector.Injector;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.ArticleContentData;
import com.gigigo.orchextra.core.sdk.model.grid.dto.ClipToPadding;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import com.gigigo.orchextra.ocmsdk.R;
import java.util.List;
import orchextra.javax.inject.Inject;

public class ContentArticleHomeLayoutView extends UiGridBaseContentData implements ArticleView {

  @Inject ContentArticleHomeLayoutViewPresenter presenter;

  private View emptyView;
  private View errorView;
  private View retryButton;
  private View moreButton;
  private View newContentContainer;

  private UiMenu uiMenu;

  private ArticleContentData articleContentData;

  private View.OnClickListener onClickRetryButtonListener = new View.OnClickListener() {
    @Override public void onClick(View v) {
      if (presenter != null) {
        presenter.reloadSection();
      }
    }
  };
  private View.OnClickListener onClickDiscoverMoreButtonListener = v -> {
    if (onLoadMoreContentListener != null) {
      onLoadMoreContentListener.onLoadMoreContent();
    }
  };


  public static ContentArticleHomeLayoutView newInstance() {
    return new ContentArticleHomeLayoutView();
  }

  public void setViewId(UiMenu uiMenu) {
    this.uiMenu = uiMenu;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.ocm_content_grid_layout_view, container, false);

    initDI();

    initView(view);
    setListeners();
    presenter.attachView(this);

    return view;
  }

  @Override public void initUi() {
    if (uiMenu != null && presenter != null) {
      presenter.setUiMenu(uiMenu);
      presenter.loadSectionFirstTime();
    }
  }

  private void initDI() {
    Injector injector = OCManager.getInjector();
    if (injector != null) {
      injector.injectContentArticleHomeLayoutView(this);
    }
  }

  private void initView(View view) {
    if (emptyView == null) {
      emptyView = view.findViewById(R.id.ocm_empty_layout);
    }
    if (errorView == null) {
      errorView = view.findViewById(R.id.ocm_error_layout);
    }
    retryButton = view.findViewById(R.id.ocm_retry_button);
    moreButton = view.findViewById(R.id.ocm_more_button);

    newContentContainer = view.findViewById(R.id.newContentContainer);
  }

  private void setListeners() {
    retryButton.setOnClickListener(onClickRetryButtonListener);
    moreButton.setOnClickListener(onClickDiscoverMoreButtonListener);
  }

  @Override public void showEmptyView(boolean isVisible) {
    if (emptyView != null) {
      emptyView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
  }

  @Override public void showArticle(List<ArticleElement<ArticleElementRender>> elements) {
    articleContentData = ArticleContentData.newInstance();

    articleContentData.addItems(elements);

    getChildFragmentManager().beginTransaction()
        .replace(R.id.listedDataContainer, articleContentData)
        .commit();
  }

  @Override public void setFilter(String filter) {

  }

  @Override
  public void setClipToPaddingBottomSize(ClipToPadding clipToPadding, int addictionalPadding) {

    if (articleContentData != null) {
      articleContentData.setClipToPaddingBottomSize(clipToPadding, addictionalPadding);
    }
  }

  @Override public void scrollToTop() {
    if (articleContentData != null) {
      articleContentData.scrollToTop();
    }
  }

  @Override public void setEmptyView(View emptyView) {
    this.emptyView = emptyView;
  }

  @Override public void setErrorView(View errorLayoutView) {
    this.errorView = errorLayoutView;
  }

  @Override public void setProgressView(View progressView) {
    //if (progressView != null) {
      //faLoading = progressView;
    //}
  }

  @Override public void reloadSection() {
    if (presenter != null) {
      presenter.reloadSection();
    }
  }

  @Override public void showNewExistingContent() {
    newContentContainer.setVisibility(View.VISIBLE);
    newContentContainer.setOnClickListener(onNewContentClickListener);
  }

  private final View.OnClickListener onNewContentClickListener = new View.OnClickListener() {
    @Override public void onClick(View v) {
      newContentContainer.setVisibility(View.GONE);
      if (presenter != null) {
        presenter.loadSection();
      }
    }
  };
}
