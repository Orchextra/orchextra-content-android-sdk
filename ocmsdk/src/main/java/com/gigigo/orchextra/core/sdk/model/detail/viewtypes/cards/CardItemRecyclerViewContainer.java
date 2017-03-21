package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import com.gigigo.orchextra.core.domain.entities.article.ArticleElement;
import com.gigigo.ui.imageloader.ImageLoader;
import java.util.List;

public class CardItemRecyclerViewContainer extends RecyclerView {

  private final Context context;
  private ImageLoader imageLoader;
  private List<ArticleElement> elements;

  public CardItemRecyclerViewContainer(Context context) {
    super(context);
    this.context = context;

    init();
  }

  public CardItemRecyclerViewContainer(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    this.context = context;

    init();
  }

  public CardItemRecyclerViewContainer(Context context, @Nullable AttributeSet attrs,
      int defStyle) {
    super(context, attrs, defStyle);
    this.context = context;

    init();
  }

  private void init() {
    setHasFixedSize(true);

    setLayoutManager(new LinearLayoutManager(this));

    CardItemRecyclerViewAdapter adapter = new CardItemRecyclerViewAdapter();
    setAdapter(adapter);

    setOnScrollListener(onScrollChangeListener);
  }

  private OnScrollListener onScrollChangeListener = new OnScrollListener() {

    @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
      super.onScrollStateChanged(recyclerView, newState);
    }

    @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
      super.onScrolled(recyclerView, dx, dy);
    }
  };

  public void setImageLoader(ImageLoader imageLoader) {
    this.imageLoader = imageLoader;
  }

  public void addArticleElementList(List<ArticleElement> elements) {
    this.elements = elements;
  }
}
