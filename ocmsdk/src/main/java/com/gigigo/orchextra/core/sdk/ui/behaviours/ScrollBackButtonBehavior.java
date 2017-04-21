package com.gigigo.orchextra.core.sdk.ui.behaviours;

import android.content.Context;
import android.support.annotation.AnimRes;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.gigigo.orchextra.ocmsdk.R;

public class ScrollBackButtonBehavior extends CoordinatorLayout.Behavior<ViewGroup> {

  private final Context context;

  public ScrollBackButtonBehavior(Context context) {
    super();
    this.context = context;
  }

  public ScrollBackButtonBehavior(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
  }

  @Override public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout,
      final ViewGroup child, final View directTargetChild, final View target,
      final int nestedScrollAxes) {
    return true;
  }

  @Override public void onNestedScroll(final CoordinatorLayout coordinatorLayout, ViewGroup child,
      View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
        dyUnconsumed);

    if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
      doHideAnimation(child);
      child.setVisibility(View.GONE);
    } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
      doVisibleAnimation(child);
      child.setVisibility(View.VISIBLE);
    }
  }

  private void doVisibleAnimation(ViewGroup child) {
    doAnimation(child, R.anim.scale_item_in);
  }

  private void doHideAnimation(ViewGroup child) {
    doAnimation(child, R.anim.scale_item_out);
  }

  private void doAnimation(ViewGroup child, @AnimRes int animRes) {
    Animation animation = AnimationUtils.loadAnimation(context, animRes);
    child.startAnimation(animation);
  }
}
