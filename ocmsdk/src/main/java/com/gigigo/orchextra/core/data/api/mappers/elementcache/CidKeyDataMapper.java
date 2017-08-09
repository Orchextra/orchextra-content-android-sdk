package com.gigigo.orchextra.core.data.api.mappers.elementcache;

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.orchextra.core.data.api.dto.elementcache.CidKeyData;
import com.gigigo.orchextra.core.domain.entities.elementcache.CidKey;

public class CidKeyDataMapper implements ExternalClassToModelMapper<CidKeyData, CidKey> {

  @Override public CidKey externalClassToModel(CidKeyData data) {
    CidKey model = new CidKey();

    if (data != null) {
      model.setSiteName(data.getSiteName());
    }

    return model;
  }
}
