package com.gigigo.orchextra.core.data.api.mappers.article;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.orchextra.core.data.api.dto.article.ApiArticleElement;
import com.gigigo.orchextra.core.data.api.dto.article.ApiArticleElementRender;
import com.gigigo.orchextra.core.domain.entities.article.ArticleButtonElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleHeaderElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageAndTextElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleRichTextElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleTextAndImageElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleVimeoVideoElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleYoutubeVideoElement;
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleButtonSize;
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleButtonType;
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement;
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleTypeSection;
import com.gigigo.orchextra.core.domain.entities.elementcache.VideoFormat;

public class ApiArticleElementMapper
    implements ExternalClassToModelMapper<ApiArticleElement, ArticleElement> {

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
      case BUTTON:
        return getArticleButtonElement(render);
    }
    return null;
  }

  private ArticleElement getArticleVideoElement(ApiArticleElementRender render) {
    //todo truchingvimeo
    // return getArticleVimeoVideoElementFAKE(render);

    VideoFormat videoFormat = VideoFormat.convertStringToType(render.getFormat());

    switch (videoFormat) {
      case YOUTUBE:
        return getArticleYoutubeVideoElement(render);
      case VIMEO:
        return getArticleVimeoVideoElement(render);
      default:
        return null;
    }
  }

  /* truchingvimeo
  @Deprecated private ArticleVimeoVideoElement getArticleVimeoVideoElementFAKE(
      ApiArticleElementRender render) {
    ArticleVimeoVideoElement element = new ArticleVimeoVideoElement();
    //todo truchingvimeo
    Random r = new Random();
    boolean random = r.nextBoolean();
    if (random) {
      element.setSource("236232109");
    } else {
      element.setSource("237059608");//vertical
    }

    return element;
  }
*/
  private ArticleVimeoVideoElement getArticleVimeoVideoElement(ApiArticleElementRender render) {
    ArticleVimeoVideoElement element = new ArticleVimeoVideoElement();
    element.setSource(render.getSource());
    return element;
  }

  private ArticleYoutubeVideoElement getArticleYoutubeVideoElement(ApiArticleElementRender render) {
    ArticleYoutubeVideoElement element = new ArticleYoutubeVideoElement();
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

  @NonNull private ArticleElement getArticleButtonElement(ApiArticleElementRender render) {
    ArticleButtonElement articleButtonElement = new ArticleButtonElement();
    articleButtonElement.setType(ArticleButtonType.convertStringToEnum(render.getType()));
    articleButtonElement.setSize(ArticleButtonSize.convertStringToEnum(render.getSize()));
    articleButtonElement.setElementUrl(render.getElementUrl());
    articleButtonElement.setText(render.getText());
    articleButtonElement.setTextColor(render.getTextColor());
    articleButtonElement.setBgColor(render.getBgColor());
    articleButtonElement.setImageUrl(render.getImageUrl());
    return articleButtonElement;
  }
}
