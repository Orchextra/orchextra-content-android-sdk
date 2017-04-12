package com.gigigo.orchextra.ocm.views;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import com.gigigo.orchextra.ocmsdk.R;

/**
 * Created by nubor on 06/04/2017.
 */

public class MoreContentListArrowView extends  android.support.v7.widget.AppCompatImageView {

  public MoreContentListArrowView(Context context) {
    super(context);
  }

  public MoreContentListArrowView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public MoreContentListArrowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }
  AnimationDrawable gyroAnimation;

  /***
   * You must to call this method in public void onWindowFocusChanged(boolean hasFocus) event of your activity
   */
  public void anim(){
    this.setBackgroundResource(R.drawable.more_contain_arrow_anim);
    gyroAnimation = (AnimationDrawable) this.getBackground();
    gyroAnimation.start();
  }


}
