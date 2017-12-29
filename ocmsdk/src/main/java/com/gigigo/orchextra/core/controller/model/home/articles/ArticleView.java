package com.gigigo.orchextra.core.controller.model.home.articles;

import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement;
import java.util.List;

public interface ArticleView {
  void initUi();

  void showEmptyView(boolean isVisible);

  void showArticle(List<ArticleElement> elements);

  void showNewExistingContent();
}
