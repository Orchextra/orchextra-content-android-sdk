package com.gigigo.orchextra.core.domain.entities.elementcache;

import java.io.Serializable;

public class FederatedAuthorization implements Serializable {

  private boolean active;
  private String type;
  private CidKey keys;

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

  public CidKey getKeys() {
    return keys;
  }

  public void setKeys(CidKey keys) {
    this.keys = keys;
  }
}
