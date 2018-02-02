package com.gigigo.orchextra.core.data.api.mappers.elementcache;

import android.util.Log;
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.orchextra.core.data.api.dto.elementcache.FederatedAuthorizationData;
import com.gigigo.orchextra.core.domain.entities.elementcache.FederatedAuthorization;

public class FederatedAuthorizationDataMapper
    implements ExternalClassToModelMapper<FederatedAuthorizationData, FederatedAuthorization> {

  private final CidKeyDataMapper mapper;

  public FederatedAuthorizationDataMapper(CidKeyDataMapper mapper) {
    this.mapper = mapper;
  }

  @Override public FederatedAuthorization externalClassToModel(FederatedAuthorizationData data) {
    final long time = System.currentTimeMillis();

    FederatedAuthorization model = new FederatedAuthorization();

    if (data != null) {
      model.setActive(data.getActive());
      model.setType(data.getType());
      model.setKeys(mapper.externalClassToModel(data.getKeys()));
    }

    Log.v("TT - FAData", (System.currentTimeMillis() - time) / 1000 + "");

    return model;
  }
}
