package com.gigigo.orchextra.ocm.views;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.gigigo.orchextra.ocmsdk.R;

/**
 * Created by nubor on 06/04/2017.
 */

public class MoreContent3ArrowView extends RelativeLayout{
  public MoreContent3ArrowView(Context context) {
    super(context);
    initView();
  }

  public MoreContent3ArrowView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView();
  }

  public MoreContent3ArrowView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView();
  }
  private void initView() {
    View view = inflate(getContext(), R.layout.more_content_3_arrows, null);
    addView(view);
  }
  public void Anim( int numRep) {

    ImageView img1 = (ImageView) findViewById(R.id.img1);
    ImageView img2 = (ImageView) findViewById(R.id.img2);
    ImageView img3 = (ImageView) findViewById(R.id.img3);
    float pixelsDp = convertDpToPixel(16, this.getContext()); //20dp img -4dp for overlay

    ObjectAnimator translationY1 = ObjectAnimator.ofFloat(img3, "translationY",0, -pixelsDp  );
    translationY1.setRepeatCount(numRep);
    translationY1.setDuration(1000);

    ObjectAnimator translationY2 = ObjectAnimator.ofFloat(img2, "translationY", -pixelsDp );
    translationY2.setRepeatCount(numRep);
    translationY2.setDuration(1000);

    ObjectAnimator alpha1 = ObjectAnimator.ofFloat(img3, "alpha", 1f, 0.5f);
    alpha1.setRepeatCount(numRep);
    alpha1.setDuration(1000);

    ObjectAnimator alpha2 = ObjectAnimator.ofFloat(img2, "alpha", 0f, 0.3f);
    alpha2.setRepeatCount(numRep);
    alpha2.setDuration(1000);

    ObjectAnimator alpha3 = ObjectAnimator.ofFloat(img1, "alpha", 0f, 0.3f);
    alpha3.setRepeatCount(numRep);
    alpha3.setDuration(1000);

    AnimatorSet sett = new AnimatorSet();
    sett.play(translationY1).with(translationY2).with(alpha1).with(alpha2).with(alpha3);
    sett.start();
  }

  public static float convertDpToPixel(float dp, Context context) {
    Resources resources = context.getResources();
    DisplayMetrics metrics = resources.getDisplayMetrics();
    float px = dp * (metrics.densityDpi / 160f);
    return px;
  }
}
