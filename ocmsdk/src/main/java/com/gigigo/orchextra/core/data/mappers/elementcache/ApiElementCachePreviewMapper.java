package com.gigigo.orchextra.core.data.mappers.elementcache;

import com.gigigo.orchextra.core.data.dto.elementcache.ApiElementCachePreview;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheBehaviour;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCachePreview;
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;

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
