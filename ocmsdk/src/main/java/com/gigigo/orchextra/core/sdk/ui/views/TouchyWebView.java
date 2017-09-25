package com.gigigo.orchextra.core.sdk.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;
import com.gigigo.ggglib.device.AndroidSdkVersion;

public class TouchyWebView extends WebView implements NestedScrollingChild {

  private int mLastY;
  private final int[] mScrollOffset = new int[2];
  private final int[] mScrollConsumed = new int[2];
  private int mNestedOffsetY;
  private NestedScrollingChildHelper mChildHelper;

  public TouchyWebView(Context context) {
    super(context);

    init();
  }

  public TouchyWebView(Context context, AttributeSet attrs) {
    super(context, attrs);

    init();
  }

  public TouchyWebView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP) private void init() {
    mChildHelper = new NestedScrollingChildHelper(this);
    if (AndroidSdkVersion.hasLollipop21()) {
      setNestedScrollingEnabled(true);
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    if (MotionEventCompat.findPointerIndex(ev, 0) == -1) {
      return super.onTouchEvent(ev);
    }

    if (ev.getPointerCount() >= 2) {
      requestDisallowInterceptTouchEvent(true);
    } else {
      requestDisallowInterceptTouchEvent(false);
    }

    boolean returnValue = false;

    MotionEvent event = MotionEvent.obtain(ev);
    final int action = MotionEventCompat.getActionMasked(event);
    if (action == MotionEvent.ACTION_DOWN) {
      mNestedOffsetY = 0;
    }
    int eventY = (int) event.getY();
    event.offsetLocation(0, mNestedOffsetY);
    switch (action) {
      case MotionEvent.ACTION_MOVE:
        int deltaY = mLastY - eventY;
        // NestedPreScroll
        if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset)) {
          deltaY -= mScrollConsumed[1];
          mLastY = eventY - mScrollOffset[1];
          event.offsetLocation(0, -mScrollOffset[1]);
          mNestedOffsetY += mScrollOffset[1];
        }
        returnValue = super.onTouchEvent(event);

        // NestedScroll
        if (dispatchNestedScroll(0, mScrollOffset[1], 0, deltaY, mScrollOffset)) {
          event.offsetLocation(0, mScrollOffset[1]);
          //mNestedOffsetY += mScrollOffset[1];
          //mLastY -= mScrollOffset[1];
        }
        break;
      case MotionEvent.ACTION_DOWN:
        returnValue = super.onTouchEvent(event);
        mLastY = eventY;
        // start NestedScroll
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
        break;
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        returnValue = super.onTouchEvent(event);
        // end NestedScroll
        stopNestedScroll();
        break;
    }
    return returnValue;

  }

  @Override
  protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
    super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    requestDisallowInterceptTouchEvent(true);


  }

  // Nested Scroll implements
  @Override
  public void setNestedScrollingEnabled(boolean enabled) {
    mChildHelper.setNestedScrollingEnabled(enabled);
  }

  @Override
  public boolean isNestedScrollingEnabled() {
    return mChildHelper.isNestedScrollingEnabled();
  }

  @Override
  public boolean startNestedScroll(int axes) {
    return mChildHelper.startNestedScroll(axes);
  }

  @Override
  public void stopNestedScroll() {
    mChildHelper.stopNestedScroll();
  }

  @Override
  public boolean hasNestedScrollingParent() {
    return mChildHelper.hasNestedScrollingParent();
  }

  @Override
  public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed,
      int[] offsetInWindow) {
    return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
  }

  @Override
  public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
    return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
  }

  @Override
  public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
    return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
  }

  @Override
  public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
    return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
  }
}