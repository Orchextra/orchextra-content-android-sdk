package com.gigigo.orchextra.core.sdk.ui.behaviours;

import android.content.Context;
import android.support.annotation.AnimRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.gigigo.orchextra.ocmsdk.R;

public class ScrollShareButtonBehavior extends CoordinatorLayout.Behavior<ImageView> {

  private final Context context;

  private State state = State.EXPANDED;
  private float savedY = 0;

  public ScrollShareButtonBehavior(Context context) {
    super();
    this.context = context;
  }

  public ScrollShareButtonBehavior(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
  }

  @Override public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout,
      final ImageView child, final View directTargetChild, final View target,
      final int nestedScrollAxes) {

    return true;
  }

  @Override
  public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, final ImageView child,
      MotionEvent ev) {

    AppBarLayout appBarLayout = (AppBarLayout) coordinatorLayout.findViewWithTag("AppBarLayout");
    if (appBarLayout != null) {
      appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
        @Override public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

          if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (state != State.COLLAPSED && child.getVisibility() == View.GONE) {
              doVisibleAnimation(child);
              child.setVisibility(View.VISIBLE);
            }
            state = State.COLLAPSED;
          } else if (verticalOffset == 0) {
            if (state != State.EXPANDED && child.getVisibility() == View.VISIBLE) {
              child.setVisibility(View.GONE);
            }
            state = State.EXPANDED;
          } else {
            if (state != State.IDLE && child.getVisibility() == View.VISIBLE) {
              doHideAnimation(child);
              child.setVisibility(View.GONE);
            }
            state = State.IDLE;
          }
        }
      });
    }

    return super.onInterceptTouchEvent(coordinatorLayout, child, ev);
  }

  @Override public void onNestedScroll(final CoordinatorLayout coordinatorLayout, ImageView child,
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

  private void doVisibleAnimation(ImageView child) {
    doAnimation(child, R.anim.scale_item_in);
  }

  private void doHideAnimation(ImageView child) {
    doAnimation(child, R.anim.scale_item_out);
  }

  private void doAnimation(ImageView child, @AnimRes int animRes) {
    Animation animation = AnimationUtils.loadAnimation(context, animRes);
    child.startAnimation(animation);
  }

  private enum State {
    COLLAPSED,
    EXPANDED,
    IDLE
  }
}
