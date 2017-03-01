package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public abstract class ArticleBaseView<T> extends FrameLayout{

  private T articleElement;
  protected View itemView;

  public ArticleBaseView(Context context, T articleElement) {
    super(context);
    this.articleElement = articleElement;

    inflateView();
  }

  private void inflateView() {
    LayoutInflater inflater =
        (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    itemView = inflater.inflate(getViewLayout(), this, true);
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();

    init();
  }

  private void init() {
    bindViews();
    bindTo(articleElement);
  }

  protected abstract int getViewLayout();
  protected abstract void bindViews();
  protected abstract void bindTo(T articleElement);
}
