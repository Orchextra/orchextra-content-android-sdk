package com.gigigo.orchextra.core.data.dto.base;

import com.gigigo.ggglib.network.responses.ApiGenericResponse;
import com.gigigo.ggglib.network.responses.HttpResponse;
import com.google.gson.annotations.SerializedName;

public class BaseApiResponse<JSONData> implements ApiGenericResponse<JSONData, ApiErrorResponse> {

  @SerializedName("status") private Boolean status;
  @SerializedName("data") private JSONData data;
  @SerializedName("error") private ApiErrorResponse error;

  private HttpResponse httpResponse;

  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  @Override public HttpResponse getHttpResponse() {
    return httpResponse;
  }

  @Override public void setHttpResponse(HttpResponse httpResponse) {
    this.httpResponse = httpResponse;
  }

  @Override public boolean isException() {
    return false;
  }

  @Override public JSONData getResult() {
    return data;
  }

  @Override public void setResult(JSONData data) {
    this.data = data;
  }

  @Override public ApiErrorResponse getBusinessError() {
    return error;
  }

  @Override public void setBusinessError(ApiErrorResponse error) {
    this.error = error;
  }
}
