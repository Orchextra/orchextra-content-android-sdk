package com.gigigo.orchextra.core.data.api.dto.base;

import com.google.gson.annotations.SerializedName;

public class ApiErrorResponse {

  @SerializedName("code") private int code;
  @SerializedName("message") private String message;

  public ApiErrorResponse(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
