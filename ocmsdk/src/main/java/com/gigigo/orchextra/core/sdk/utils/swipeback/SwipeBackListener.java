package com.gigigo.orchextra.core.sdk.utils.swipeback;

public interface SwipeBackListener {

  /**
   * Return scrolled fraction of the layout.
   *
   * @param fractionAnchor relative to the anchor.
   * @param fractionScreen relative to the screen.
   */
  void onViewPositionChanged(float fractionAnchor, float fractionScreen);

}