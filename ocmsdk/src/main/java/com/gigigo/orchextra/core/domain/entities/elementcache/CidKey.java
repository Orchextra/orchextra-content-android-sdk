package com.gigigo.orchextra.core.domain.entities.elementcache;

import java.io.Serializable;

public class CidKey implements Serializable {

  private String siteName;

  public String getSiteName() {
    return siteName;
  }

  public void setSiteName(String siteName) {
    this.siteName = siteName;
  }
}
