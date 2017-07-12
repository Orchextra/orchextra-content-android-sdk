package com.gigigo.orchextra.core.domain.entities.ocm;

public class OxSession {
  private String key;
  private String secret;
  private String token;

  public void setCredentials(String key, String secret) {
    this.key = key;
    this.secret = secret;
  }

  public String getKey() {

    return key;
  }

  public String getSecret() {
    return secret;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getAccessToken() {
    return "Bearer " + token;
  }
}
