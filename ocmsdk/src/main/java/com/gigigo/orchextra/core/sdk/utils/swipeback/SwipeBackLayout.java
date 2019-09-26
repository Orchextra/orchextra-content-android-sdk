package com.gigigo.orchextra.core.sdk.utils.swipeback;

import android.app.Activity;
import android.content.Context;
import com.google.android.material.appbar.AppBarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.customview.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ScrollView;

//'com.github.liuguangqiang.swipeback:library:1.0.2@aar'
public class SwipeBackLayout extends ViewGroup {

  private static final String TAG = "SwipeBackLayout";

  private static final double AUTO_FINISHED_SPEED_LIMIT = 2000.0;

  private static final float BACK_FACTOR = 0.5f;

  private final ViewDragHelper viewDragHelper;
  float lastY = 0;
  float newY = 0;
  float offsetY = 0;
  float lastX = 0;
  float newX = 0;
  float offsetX = 0;
  private View target;
  private View scrollChild;
  private int verticalDragRange = 0;
  private int horizontalDragRange = 0;
  private int draggingState = 0;
  private int draggingOffset;
  private float firstX = -1, firstY = -1;
  private float minPositionBorderSwipe = 200;
  /**
   * Whether allow to pull this layout.
   */
  private boolean enablePullToBack = true;
  /**
   * the anchor of calling finish.
   */
  private float finishAnchor = 300;
  private SwipeBackListener swipeBackListener;

  public SwipeBackLayout(Context context) {
    this(context, null);
  }

  public SwipeBackLayout(Context context, AttributeSet attrs) {
    super(context, attrs);

    viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelperCallBack());
    chkDragable();
  }

  public void setOnSwipeBackListener(SwipeBackListener listener) {
    swipeBackListener = listener;
  }

  private void chkDragable() {
    setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
          lastX = motionEvent.getRawX();
          newY = motionEvent.getRawY();

          offsetY = Math.abs(newY - lastY);
          lastY = newY;

          offsetX = Math.abs(newX - lastX);
          lastX = newX;

          setEnablePullToBack(offsetY < offsetX);
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
          firstX = -1;
          firstY = -1;
        }

        return false;
      }
    });
  }

  public void setEnablePullToBack(boolean b) {
    enablePullToBack = b;
    Log.i(TAG, "enablePullToBack:" + enablePullToBack);
  }

  private void ensureTarget() {
    if (target == null) {
      if (getChildCount() > 1) {
        throw new IllegalStateException("SwipeBackLayout must contains only one direct child");
      }
      target = getChildAt(0);

      if (scrollChild == null && target != null) {
        if (target instanceof ViewGroup) {
          findScrollView((ViewGroup) target);
        } else {
          scrollChild = target;
        }
      }
    }
  }

  /**
   * Find out the scrollable child view from a ViewGroup.
   */
  private void findScrollView(ViewGroup viewGroup) {
    scrollChild = viewGroup;
    if (viewGroup.getChildCount() > 0) {
      int count = viewGroup.getChildCount();
      View child;
      for (int i = 0; i < count; i++) {
        child = viewGroup.getChildAt(i);
        if (child instanceof AbsListView
            || child instanceof ScrollView
            || child instanceof ViewPager
            || child instanceof WebView
            || child instanceof CoordinatorLayout) {
          scrollChild = child;
          return;
        } else if (scrollChild != null && child instanceof ViewGroup) {
          findScrollView((ViewGroup) child);
        }
      }
    }
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    int width = getMeasuredWidth();
    int height = getMeasuredHeight();
    if (getChildCount() == 0) return;

    View child = getChildAt(0);

    int childWidth = width - getPaddingLeft() - getPaddingRight();
    int childHeight = height - getPaddingTop() - getPaddingBottom();
    int childLeft = getPaddingLeft();
    int childTop = getPaddingTop();
    int childRight = childLeft + childWidth;
    int childBottom = childTop + childHeight;
    child.layout(childLeft, childTop, childRight, childBottom);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    if (getChildCount() > 1) {
      throw new IllegalStateException("SwipeBackLayout must contains only one direct child.");
    }

    if (getChildCount() > 0) {
      int measureWidth =
          MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
              MeasureSpec.EXACTLY);
      int measureHeight =
          MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(),
              MeasureSpec.EXACTLY);
      getChildAt(0).measure(measureWidth, measureHeight);
    }
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    verticalDragRange = h;
    horizontalDragRange = w;

    finishAnchor = finishAnchor > 0 ? finishAnchor : horizontalDragRange * BACK_FACTOR;
  }

  private int getDragRange() {
    return horizontalDragRange;
  }

  @Override public boolean onInterceptTouchEvent(MotionEvent event) {
    boolean handled = false;
    ensureTarget();
    if (isEnabled()) {
      handled = viewDragHelper.shouldInterceptTouchEvent(event);
    } else {
      viewDragHelper.cancel();
    }

    if (firstX == -1 || firstY == -1) {
      firstX = event.getX();
      firstY = event.getY();
    }

    boolean isSwipeInAxisX = Math.abs(event.getX() - firstX) > Math.abs(event.getY() - firstY);

    if (event.getAction() == MotionEvent.ACTION_UP) {
      firstX = -1;
      firstY = -1;
      return false;
    }

    if (event.getX() > minPositionBorderSwipe && isSwipeInAxisX) return false;

    return handled || super.onInterceptTouchEvent(event);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    viewDragHelper.processTouchEvent(event);

    if (firstX == -1 || firstY == -1) {
      firstX = event.getX();
      firstY = event.getY();
    }

    boolean isSwipeInAxisX = Math.abs(event.getX() - firstX) > Math.abs(event.getY() - firstY);

    if (event.getAction() == MotionEvent.ACTION_UP) {
      firstX = -1;
      firstY = -1;
      return false;
    }

    if (event.getX() > minPositionBorderSwipe && isSwipeInAxisX) return false;

    return true;
  }

  @Override public void computeScroll() {
    if (viewDragHelper.continueSettling(true)) {
      ViewCompat.postInvalidateOnAnimation(this);
    }
  }

  public boolean canChildScrollUp() {
    return ViewCompat.canScrollVertically(scrollChild, -1);
  }

  private boolean canChildScrollRight() {
    return ViewCompat.canScrollHorizontally(scrollChild, -1);
  }

  private boolean canChildScrollLeft() {
    return ViewCompat.canScrollHorizontally(scrollChild, 1);
  }

  private void finish() {
    Activity act = (Activity) getContext();
    act.finish();
    act.overridePendingTransition(0, android.R.anim.fade_out);
  }

  private class ViewDragHelperCallBack extends ViewDragHelper.Callback {

    @Override public boolean tryCaptureView(View child, int pointerId) {
      return child == target && enablePullToBack;
    }

    @Override public int getViewVerticalDragRange(View child) {
      return verticalDragRange;
    }

    @Override public int getViewHorizontalDragRange(View child) {
      return horizontalDragRange;
    }

    @Override public int clampViewPositionHorizontal(View child, int left, int dx) {

      int result = 0;

      if (firstX < 0 || firstX > minPositionBorderSwipe) return result;

      int leftBound;
      int rightBound;
      if (!canChildScrollRight() && left > 0) {
        leftBound = getPaddingLeft();
        rightBound = horizontalDragRange;
        result = Math.min(Math.max(left, leftBound), rightBound);
      }

      return result;
    }

    @Override public void onViewDragStateChanged(int state) {
      if (state == draggingState) return;

      if ((draggingState == ViewDragHelper.STATE_DRAGGING
          || draggingState == ViewDragHelper.STATE_SETTLING)
          && state == ViewDragHelper.STATE_IDLE) {
        // the view stopped from moving.
        if (draggingOffset == getDragRange()) {
          finish();
        }
      }

      draggingState = state;
    }

    @Override
    public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
      draggingOffset = Math.abs(left);

      //The proportion of the sliding.
      float fractionAnchor = (float) draggingOffset / finishAnchor;
      if (fractionAnchor >= 1) fractionAnchor = 1;

      float fractionScreen = (float) draggingOffset / (float) getDragRange();
      if (fractionScreen >= 1) fractionScreen = 1;

      if (swipeBackListener != null) {
        swipeBackListener.onViewPositionChanged(fractionAnchor, fractionScreen);
      }
    }

    @Override public void onViewReleased(View releasedChild, float xvel, float yvel) {
      if (draggingOffset == 0) return;

      if (draggingOffset == getDragRange()) return;

      boolean isBack = false;

      if (backBySpeed(xvel, yvel)) {
        isBack = !canChildScrollUp();
      } else if (draggingOffset >= finishAnchor) {
        isBack = true;
      } else if (draggingOffset < finishAnchor) {
        isBack = false;
      }

      int finalLeft = isBack ? horizontalDragRange : 0;
      smoothScrollToX(finalLeft);

      AppBarLayout appBarLayout = findAppBarLayout((ViewGroup) target);
      if (appBarLayout != null && Math.abs(appBarLayout.getY()) < appBarLayout.getTotalScrollRange()) {
        if (Math.abs(appBarLayout.getY()) / 2 >= appBarLayout.getTotalScrollRange()) {
          appBarLayout.setExpanded(false, true);
        } else {
          appBarLayout.setExpanded(true, true);
        }
      }
    }

    private AppBarLayout findAppBarLayout(ViewGroup viewGroup) {
      scrollChild = viewGroup;
      if (viewGroup.getChildCount() > 0) {
        int count = viewGroup.getChildCount();
        View child;
        for (int i = 0; i < count; i++) {
          child = viewGroup.getChildAt(i);
          if (child instanceof AppBarLayout) {
            return (AppBarLayout) child;
          } else if (scrollChild != null && child instanceof ViewGroup) {
            return findAppBarLayout((ViewGroup) child);
          }
        }
      }
      return null;
    }

    private boolean backBySpeed(float xvel, float yvel) {
      return Math.abs(xvel) > Math.abs(yvel)
          && Math.abs(xvel) > AUTO_FINISHED_SPEED_LIMIT
          && !canChildScrollLeft();
    }

    private void smoothScrollToX(int finalLeft) {
      if (viewDragHelper.settleCapturedViewAt(finalLeft, 0)) {
        ViewCompat.postInvalidateOnAnimation(SwipeBackLayout.this);
      }
    }
  }
}
