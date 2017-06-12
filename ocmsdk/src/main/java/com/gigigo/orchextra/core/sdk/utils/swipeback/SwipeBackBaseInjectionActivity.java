package com.gigigo.orchextra.core.sdk.utils.swipeback;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.gigigo.orchextra.core.sdk.di.base.BaseInjectionActivity;

//'com.github.liuguangqiang.swipeback:library:1.0.2@aar'
public abstract class SwipeBackBaseInjectionActivity<T> extends BaseInjectionActivity<T>
    implements SwipeBackListener {

  private static final DragEdge DEFAULT_DRAG_EDGE = DragEdge.LEFT;

  private SwipeBackLayout swipeBackLayout;
  private ImageView ivShadow;

  @Override public void setContentView(int layoutResID) {
    super.setContentView(getContainer());
    View view = LayoutInflater.from(this).inflate(layoutResID, null);
    swipeBackLayout.addView(view);
  }

  private View getContainer() {
    RelativeLayout container = new RelativeLayout(this);
    swipeBackLayout = new SwipeBackLayout(this);
    //swipeBackLayout.setDragEdge(DEFAULT_DRAG_EDGE);
    swipeBackLayout.setOnSwipeBackListener(this);
    ivShadow = new ImageView(this);
    //ivShadow.setBackgroundColor(getResources().getColor(R.color.black_p50));
    LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    container.addView(ivShadow, params);
    container.addView(swipeBackLayout);
    return container;
  }

  public void setEnableSwipe(boolean enableSwipe) {
    swipeBackLayout.setEnablePullToBack(enableSwipe);
  }

  public void setDragEdge(DragEdge dragEdge) {
    //swipeBackLayout.setDragEdge(dragEdge);
  }

  public SwipeBackLayout getSwipeBackLayout() {
    return swipeBackLayout;
  }

  @Override public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
    ivShadow.setAlpha(1 - fractionScreen);
  }
}