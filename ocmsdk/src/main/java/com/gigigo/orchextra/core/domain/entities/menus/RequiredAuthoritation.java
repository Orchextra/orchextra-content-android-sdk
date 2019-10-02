package com.gigigo.orchextra.core.domain.entities.menus;

import androidx.annotation.NonNull;

public enum RequiredAuthoritation {
  ALL("all"), LOGGED("logged"), NONE("");

  private final String authoritation;

  RequiredAuthoritation(String authoritation) {
    this.authoritation = authoritation;
  }

  public String getAuthoritation() {
    return authoritation;
  }

  public static RequiredAuthoritation convert(@NonNull String requiredAuth) {
    RequiredAuthoritation[] values = RequiredAuthoritation.values();
    for (RequiredAuthoritation value : values) {
      if (requiredAuth.equals(value.getAuthoritation())) {
        return value;
      }
    }
    return NONE;
  }
}