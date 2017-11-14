package com.gigigo.orchextra.core.data.api.dto.versioning;

import com.gigigo.orchextra.core.data.rxCache.OcmCacheImp;
import com.google.gson.annotations.SerializedName;
import com.mskn73.kache.Kacheable;
import org.jetbrains.annotations.NotNull;

public class ApiVersionResponse {
  @SerializedName("status") private Boolean status;
  @SerializedName("data") private String data;

  public Boolean getStatus() {
    return status;
  }

  public String getData() {
    return data;
  }

}
