package com.gigigo.orchextra.core.data.api.dto.versioning;

import com.google.gson.annotations.SerializedName;

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
