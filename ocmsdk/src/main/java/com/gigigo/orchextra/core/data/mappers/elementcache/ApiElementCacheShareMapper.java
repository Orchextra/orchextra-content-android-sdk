package com.gigigo.orchextra.core.data.mappers.elementcache;

import com.gigigo.orchextra.core.data.dto.elementcache.ApiElementCacheShare;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheShare;
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;

/**
 * Created by rui.alonso on 5/12/16.
 */
public class ApiElementCacheShareMapper implements
    ExternalClassToModelMapper<ApiElementCacheShare, ElementCacheShare> {

  @Override public ElementCacheShare externalClassToModel(ApiElementCacheShare data) {
    ElementCacheShare model = new ElementCacheShare();

    model.setUrl(data.getUrl());
    model.setText(data.getText());

    return model;
  }
}
