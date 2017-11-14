package com.gigigo.orchextra.core.data.api.dto.versioning;

import com.gigigo.orchextra.core.data.rxCache.OcmCacheImp;
import com.mskn73.kache.Kacheable;
import org.jetbrains.annotations.NotNull;

public class ApiVersionKache implements Kacheable {

  private String version;

  public ApiVersionKache(String version) {
    this.version = version;
  }

  @NotNull @Override public String getKey() {
    return OcmCacheImp.VERSION_KEY;
  }

  public String getVersion() {
    return version;
  }
}
