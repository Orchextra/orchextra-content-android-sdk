package com.gigigo.orchextra.core.sdk.ui.behaviours;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.gigigo.orchextra.core.sdk.ui.views.toolbars.DetailToolbarView;

public class DetailToolbarViewBehaviour extends CoordinatorLayout.Behavior<DetailToolbarView>{

  private final Context context;

  private State state = State.EXPANDED;

  public DetailToolbarViewBehaviour(Context context) {
    this.context = context;
  }

  public DetailToolbarViewBehaviour(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
  }

  @Override
  public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, final DetailToolbarView child,
      MotionEvent ev) {

    AppBarLayout appBarLayout = (AppBarLayout) coordinatorLayout.findViewWithTag("AppBarLayout");
    if (appBarLayout != null) {
      appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
        @Override public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

          if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (state != State.COLLAPSED) {
              switchDetailViewItems(child, false);
            }
            state = State.COLLAPSED;
          } else if (verticalOffset == 0) {
            if (state != State.EXPANDED) {
              //switchDetailViewItems(child, true);
            }
            state = State.EXPANDED;
          } else {
            if (state != State.IDLE) {
              switchDetailViewItems(child, true);
            }
            state = State.IDLE;
          }
        }
      });
    }

    return super.onInterceptTouchEvent(coordinatorLayout, child, ev);
  }

  private void switchDetailViewItems(DetailToolbarView child, boolean expanded) {
    child.switchBetweenButtonAndToolbar(!expanded);
  }

  private enum State {
    COLLAPSED,
    EXPANDED,
    IDLE
  }
}
