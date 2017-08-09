package com.gigigo.orchextra.core.data.api.mappers.elementcache;

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
    FederatedAuthorization model = new FederatedAuthorization();

    if (data != null) {
      model.setActive(data.isActive());
      model.setType(data.getType());
      model.setKeys(mapper.externalClassToModel(data.getKeys()));
    }

    return model;
  }
}
