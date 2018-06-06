package com.gigigo.orchextra.core.domain.entities.elements;

import com.gigigo.orchextra.core.domain.entities.menus.RequiredAuthoritation;
import java.io.Serializable;

public class ElementSegmentation implements Serializable {

  private RequiredAuthoritation requiredAuth;

  public RequiredAuthoritation getRequiredAuth() {
    return requiredAuth;
  }

  public void setRequiredAuth(RequiredAuthoritation requiredAuth) {
    this.requiredAuth = requiredAuth;
  }
}
