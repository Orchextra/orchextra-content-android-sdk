package com.gigigo.orchextra.core.domain.entities.menus;

public enum RequiredAuthoritation {
  ALL("all"),
  LOGGED("logged"),
  NONE("");

  private final String authoritation;

  RequiredAuthoritation(String authoritation) {
    this.authoritation = authoritation;
  }

  public String getAuthoritation() {
    return authoritation;
  }

  public static RequiredAuthoritation convert(String requiredAuth) {
    RequiredAuthoritation[] values = RequiredAuthoritation.values();
    for (RequiredAuthoritation value : values) {
      if (requiredAuth.equals(value.getAuthoritation())) {
        return value;
      }
    }
    return NONE;
  }
}
