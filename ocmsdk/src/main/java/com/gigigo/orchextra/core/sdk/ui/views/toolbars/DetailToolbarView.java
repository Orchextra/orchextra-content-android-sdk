package com.gigigo.orchextra.core.sdk.ui.views.toolbars;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import com.gigigo.orchextra.ocmsdk.R;

public class DetailToolbarView extends FrameLayout {

  private final Context context;

  private View detailToolbar;
  private View backToolbarButton;
  private View shareToolbarButton;
  private View backToolbarBgButton;
  private View shareToolbarBgButton;

  private boolean isShareable;


  public DetailToolbarView(@NonNull Context context) {
    super(context);
    this.context = context;

    init();
  }

  public DetailToolbarView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    this.context = context;

    init();
  }

  public DetailToolbarView(@NonNull Context context, @Nullable AttributeSet attrs,
      int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.context = context;

    init();
  }

  private void init() {
    initViews();
  }

  private void initViews() {
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.view_detail_toolbar_layout, this, true);

    detailToolbar = view.findViewById(R.id.detailToolbar);
    backToolbarButton = view.findViewById(R.id.back_toolbar_button);
    shareToolbarButton = view.findViewById(R.id.share_toolbar_button);
    backToolbarBgButton = view.findViewById(R.id.back_toolbar_bg_button);
    shareToolbarBgButton = view.findViewById(R.id.share_bg_toolbar_button);
  }

  public void switchBetweenButtonAndToolbar(boolean areVisibleToolbar) {
    detailToolbar.setVisibility(areVisibleToolbar ? View.VISIBLE: View.GONE);
    backToolbarBgButton.setVisibility(!areVisibleToolbar ? View.VISIBLE: View.INVISIBLE);
    shareToolbarBgButton.setVisibility(!areVisibleToolbar ? View.VISIBLE: View.INVISIBLE);

    //backToolbarButton.setVisibility(!areVisibleButtons ? View.VISIBLE : View.GONE);
    //shareToolbarButton.setVisibility(!areVisibleButtons ? View.VISIBLE : View.GONE);

    //detailToolbar.animate().translationY(0).setListener(new AnimatorListenerAdapter() {
    //  @Override public void onAnimationEnd(Animator animation) {
    //    super.onAnimationEnd(animation);
    //    detailToolbar.setVisibility(View.GONE);
    //  }
    //});
  }

  public void setOnClickBackButtonListener(OnClickListener onClickBackButtonListener) {
    backToolbarButton.setOnClickListener(onClickBackButtonListener);
  }

  public void setOnClickShareButtonListener(OnClickListener onClickShareButtonListener) {
    shareToolbarButton.setOnClickListener(onClickShareButtonListener);
  }

  public void setShareButtonVisible(boolean shareButtonVisible) {
    this.isShareable = shareButtonVisible;
    shareToolbarButton.setVisibility(shareButtonVisible ? View.VISIBLE : View.GONE);
  }
}
