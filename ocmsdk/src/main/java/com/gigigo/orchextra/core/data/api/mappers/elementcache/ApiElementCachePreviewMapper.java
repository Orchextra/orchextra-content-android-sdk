package com.gigigo.orchextra.core.data.api.mappers.elementcache;

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.orchextra.core.data.api.dto.elementcache.ApiElementCachePreview;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheBehaviour;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCachePreview;

public class ApiElementCachePreviewMapper
  implements ExternalClassToModelMapper<ApiElementCachePreview, ElementCachePreview> {

  @Override public ElementCachePreview externalClassToModel(ApiElementCachePreview data) {
    ElementCachePreview model = new ElementCachePreview();

    model.setText(data.getText());
    model.setImageUrl(data.getImageUrl());
    model.setImageThumb(data.getImageThumb());
    model.setBehaviour(ElementCacheBehaviour.convertStringToEnum(data.getBehaviour()));

    return model;
  }
}
