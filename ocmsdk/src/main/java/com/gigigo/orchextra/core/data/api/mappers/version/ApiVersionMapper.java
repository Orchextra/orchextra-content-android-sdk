package com.gigigo.orchextra.core.data.api.mappers.version;

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.orchextra.core.data.api.dto.versioning.ApiVersionKache;
import com.gigigo.orchextra.core.domain.entities.version.VersionData;
import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Singleton;

@Singleton
public class ApiVersionMapper
    implements ExternalClassToModelMapper<ApiVersionKache, VersionData> {

  @Inject
  public ApiVersionMapper() {

  }

  @Override public VersionData externalClassToModel(ApiVersionKache data) {
    VersionData versionData = new VersionData();
    versionData.setVersion(data.getVersion());
    return versionData;
  }
}
