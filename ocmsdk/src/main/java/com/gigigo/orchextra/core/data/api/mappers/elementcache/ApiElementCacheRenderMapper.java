package com.gigigo.orchextra.core.data.api.mappers.elementcache;

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.orchextra.core.data.api.dto.article.ApiArticleElement;
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCacheRender;
import com.gigigo.orchextra.core.data.api.mappers.article.ApiArticleElementMapper;
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheRender;
import com.gigigo.orchextra.core.domain.entities.elementcache.VideoFormat;
import java.util.ArrayList;
import java.util.List;

public class ApiElementCacheRenderMapper
    implements ExternalClassToModelMapper<ApiElementCacheRender, ElementCacheRender> {

  private final ApiArticleElementMapper apiArticleElementMapper;
  private final FederatedAuthorizationDataMapper federatedAuthorizationDataMapper;

  public ApiElementCacheRenderMapper(ApiArticleElementMapper apiArticleElementMapper,
      FederatedAuthorizationDataMapper federatedAuthorizationDataMapper) {
    this.apiArticleElementMapper = apiArticleElementMapper;
    this.federatedAuthorizationDataMapper = federatedAuthorizationDataMapper;
  }

  @Override public ElementCacheRender externalClassToModel(ApiElementCacheRender data) {
    ElementCacheRender model = new ElementCacheRender();

    model.setContentUrl(data.getContentUrl());
    model.setUrl(data.getUrl());

    model.setTitle(data.getTitle());

    model.setSource(data.getSource());
    if (data.getFormat() != null) {
      model.setFormat(VideoFormat.convertStringToType(data.getFormat()));
    }

    model.setSchemeUri(data.getSchemeUri());

    List<ArticleElement> articleElementList = new ArrayList<>();
    if (data.getElements() != null) {
      for (ApiArticleElement apiArticleElement : data.getElements()) {
        ArticleElement articleElement =
            apiArticleElementMapper.externalClassToModel(apiArticleElement);

        if (articleElement != null) {
          articleElementList.add(articleElement);
        }
      }

      //ArticleButtonElement articleButtonElement = new ArticleButtonElement();
      //articleButtonElement.setType(ArticleButtonType.IMAGE);
      //articleButtonElement.setSize(ArticleButtonSize.MEDIUM);
      //articleButtonElement.setElementUrl("element/article/THE-7-BEST-COLLABORATIONS-OF-2016-SkWhR4W4g");
      //articleButtonElement.setText("Ver en mapa");
      //articleButtonElement.setTextColor("#FF4422");
      //articleButtonElement.setBgColor("#4422FF");
      //articleButtonElement.setImageUrl("https://i.ytimg.com/vi/kzvdmSA13Nc/maxresdefault.jpg");
      //articleElementList.add(articleButtonElement);
    }

    model.setFederatedAuth(
        federatedAuthorizationDataMapper.externalClassToModel(data.getFederatedAuth()));

    model.setElements(articleElementList);

    return model;
  }
}
