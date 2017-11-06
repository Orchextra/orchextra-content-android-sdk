package com.gigigo.orchextra.core.domain.entities.cid;

import java.util.HashMap;
import java.util.Map;

public class User {
  private String uuid;
  private Map<String, Object> additionalProperties = new HashMap<>();

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }
}
