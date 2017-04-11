package com.gigigo.orchextra.core.data.api.mappers.article;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.orchextra.core.data.api.dto.article.ApiArticleElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleHeaderElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageAndTextElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleTextAndImageElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleVideoElement;
import com.gigigo.orchextra.core.data.api.dto.article.ApiArticleElementRender;
import com.gigigo.orchextra.core.domain.entities.article.ArticleElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleRichTextElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleTypeSection;

public class ApiArticleElementMapper implements
    ExternalClassToModelMapper<ApiArticleElement, ArticleElement> {

  @Override public ArticleElement externalClassToModel(ApiArticleElement data) {

    String apiType = data.getType();
    ArticleTypeSection articleTypeSection = ArticleTypeSection.convertStringToEnum(apiType);

    ApiArticleElementRender render = data.getRender();

    if (render != null) {
      return convertArticleByType(articleTypeSection, render);
    }

    return null;
  }

  @Nullable private ArticleElement convertArticleByType(ArticleTypeSection articleTypeSection,
      ApiArticleElementRender render) {
    switch (articleTypeSection) {
      case HEADER:
        return getArticleHeaderElement(render);
      case IMAGE:
        return getArticleImageElement(render);
      case VIDEO:
        return getArticleVideoElement(render);
      case RICH_TEXT:
        return getArticleRichTextElement(render);
      case IMAGE_AND_TEXT:
        return getArticleImageAndTextElement(render);
      case TEXT_AND_IMAGE:
        return getArticleTextAndImageElement(render);
    }
    return null;
  }

  @NonNull private ArticleElement getArticleVideoElement(ApiArticleElementRender render) {
    ArticleVideoElement element = new ArticleVideoElement();
    element.setImageUrl(render.getImageUrl());
    element.setFormat(render.getFormat());
    element.setSource(render.getSource());
    return element;
  }

  @NonNull private ArticleElement getArticleRichTextElement(ApiArticleElementRender render) {
    ArticleRichTextElement element = new ArticleRichTextElement();
    element.setHtml(render.getText());
    return element;
  }

  @NonNull private ArticleElement getArticleImageElement(ApiArticleElementRender data) {
    ArticleImageElement element = new ArticleImageElement();
    element.setImageUrl(data.getImageUrl());
    element.setImageThumb(data.getImageThumb());
    element.setElementUrl(data.getElementUrl());
    return element;
  }

  @NonNull private ArticleElement getArticleHeaderElement(ApiArticleElementRender data) {
    ArticleHeaderElement element = new ArticleHeaderElement();
    element.setHtml(data.getText());
    element.setImageUrl(data.getImageUrl());
    element.setImageThumb(data.getImageThumb());
    return element;
  }

  @NonNull private ArticleElement getArticleImageAndTextElement(ApiArticleElementRender render) {
    ArticleImageAndTextElement element = new ArticleImageAndTextElement();
    element.setText(render.getText());
    element.setImageUrl(render.getImageUrl());
    element.setRatios(render.getRatios());
    return element;
  }

  @NonNull private ArticleElement getArticleTextAndImageElement(ApiArticleElementRender render) {
    ArticleTextAndImageElement element = new ArticleTextAndImageElement();
    element.setText(render.getText());
    element.setImageUrl(render.getImageUrl());
    element.setRatios(render.getRatios());
    return element;
  }

}
