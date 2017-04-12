package com.gigigo.orchextra.ocm.views;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

/**
 * Created by nubor on 05/04/2017.
 */

public class MoreContentArrowView extends android.support.v7.widget.AppCompatImageView {

  public MoreContentArrowView(Context context) {
    super(context);
  }

  public MoreContentArrowView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public MoreContentArrowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void Anim(float dp,int numRepe) {

    ObjectAnimator animator1 =
        ObjectAnimator.ofFloat(this, "translationY", -1 * convertDpToPixel(dp, this.getContext()));
    animator1.setRepeatCount(numRepe);
    animator1.setDuration(1000);

    ObjectAnimator animator2 = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
    animator2.setRepeatCount(numRepe);
    animator2.setDuration(1000);

    AnimatorSet sett = new AnimatorSet();
    sett.play(animator1).with(animator2);
    sett.start();
  }

  public static float convertDpToPixel(float dp, Context context) {
    Resources resources = context.getResources();
    DisplayMetrics metrics = resources.getDisplayMetrics();
    float px = dp * (metrics.densityDpi / 160f);
    return px;
  }
}
