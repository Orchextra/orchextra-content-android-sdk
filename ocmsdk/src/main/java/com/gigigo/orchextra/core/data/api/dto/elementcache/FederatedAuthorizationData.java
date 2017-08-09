package com.gigigo.orchextra.core.data.api.dto.elementcache;

public class FederatedAuthorizationData {

  private boolean active;
  private String type;
  private CidKeyData keys;

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public CidKeyData getKeys() {
    return keys;
  }

  public void setKeys(CidKeyData keys) {
    this.keys = keys;
  }
}
