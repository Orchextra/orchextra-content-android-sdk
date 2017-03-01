package com.gigigo.orchextra.core.domain.entities.elementcache;

public enum ElementCacheBehaviour {
  CLICK("click"),
  SWIPE("swipe"),
  NONE("");

  private final String behaviour;

  ElementCacheBehaviour(String behaviour) {
    this.behaviour = behaviour;
  }

  public String getBehaviour() {
    return behaviour;
  }

  public static ElementCacheBehaviour convertStringToEnum(String behaviour) {
    ElementCacheBehaviour[] values = ElementCacheBehaviour.values();
    for (ElementCacheBehaviour value : values) {
      if (value.getBehaviour().equals(behaviour)) {
        return value;
      }
    }
    return NONE;
  }
}
