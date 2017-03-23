package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.gigigo.orchextra.ocmsdk.R;

public class VerticalViewPager extends ViewPager {

  public static final int HORIZONTAL = 0;
  public static final int VERTICAL = 1;

  static final int MIN_DISTANCE = 100;
  private float oldX, oldY;

  private int mSwipeOrientation;

  public VerticalViewPager(Context context) {
    super(context);
    mSwipeOrientation = HORIZONTAL;
  }

  public VerticalViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
    setSwipeOrientation(context, attrs);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    return super.onTouchEvent(mSwipeOrientation == VERTICAL ? swapXY(event) : event);
  }

  @Override public boolean onInterceptTouchEvent(MotionEvent event) {
    if (mSwipeOrientation == VERTICAL) {
      int isVerticalSwipe = detectVerticalSwipe(event);

      return isVerticalSwipe != -1 || super.onInterceptTouchEvent(event);

      //boolean intercepted = super.onInterceptHoverEvent(swapXY(event));
      //swapXY(event);
      //return intercepted;
    }
    return super.onInterceptTouchEvent(event);
  }

  private int detectVerticalSwipe(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        oldX = event.getX();
        oldY = event.getY();
      case MotionEvent.ACTION_MOVE:
      case MotionEvent.ACTION_UP:

        float currentX = event.getX();
        float deltaX = currentX - oldX;
        if (Math.abs(deltaX) > MIN_DISTANCE) {
          return 0;
        }

        float currentY = event.getY();
        float deltaY = currentY - oldY;
        if (Math.abs(deltaY) > MIN_DISTANCE) {
          return 1;
        }

        break;
    }
    return -1;
  }

  public void setSwipeOrientation(int swipeOrientation) {
    if (swipeOrientation == HORIZONTAL || swipeOrientation == VERTICAL) {
      mSwipeOrientation = swipeOrientation;
    } else {
      throw new IllegalStateException("Swipe Orientation can be either CustomViewPager.HORIZONTAL"
          + " or CustomViewPager.VERTICAL");
    }
    initSwipeMethods();
  }

  private void setSwipeOrientation(Context context, AttributeSet attrs) {
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerticalViewPager);
    mSwipeOrientation = typedArray.getInteger(R.styleable.VerticalViewPager_swipe_orientation, 0);
    typedArray.recycle();
    initSwipeMethods();
  }

  private void initSwipeMethods() {
    if (mSwipeOrientation == VERTICAL) {
      // The majority of the work is done over here
      setPageTransformer(true, new VerticalPageTransformer());
      // The easiest way to get rid of the overscroll drawing that happens on the left and right
      setOverScrollMode(OVER_SCROLL_NEVER);
    }
  }

  private MotionEvent swapXY(MotionEvent event) {
    float width = getWidth();
    float height = getHeight();

    float newX = (event.getY() / height) * width;
    float newY = (event.getX() / width) * height;

    event.setLocation(newX, newY);
    return event;
  }
}
