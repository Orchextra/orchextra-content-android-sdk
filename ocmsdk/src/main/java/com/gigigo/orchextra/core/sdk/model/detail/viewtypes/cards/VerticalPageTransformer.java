package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;

public class VerticalPageTransformer implements ViewPager.PageTransformer {

  @Override public void transformPage(View page, float position) {
    //if (position < -1) {
    //  // This page is way off-screen to the left
    //  page.setAlpha(0);
    //} else if (position <= 1) {
    //  page.setAlpha(1);
    //
    //  // Counteract the default slide transition
    //  page.setTranslationX(page.getWidth() * -position);
    //
    //  // set Y position to swipe in from top
    //  float yPosition = position * page.getHeight();
    //  page.setTranslationY(yPosition);
    //} else {
    //  // This page is way off screen to the right
    //  page.setAlpha(0);
    //}

    page.setTranslationX(page.getWidth() * -position);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      if (position >= 0) {
        page.setTranslationY(position * page.getHeight());
        page.setTranslationZ(10f);
        page.setAlpha(1f);
      } else {
        page.setTranslationY(0f);
        page.setTranslationZ(9.9f);
      }
      page.setAlpha(1f);
    }

    //float result = position > 0 ? - page.getHeight() + position * page.getHeight() : 0f;
    //Log.i("PAGE", "page:" + result);

    //page.setTranslationY(position < 0 ? position * page.getHeight() : 0f);
    //page.setTranslationY(result);
  }

  //Position of page relative to the current front-and-center position of the pager.
  // 0 is front and center.
  // 1 is one full page position to the right
  // -1 is one page position to the left.
}
