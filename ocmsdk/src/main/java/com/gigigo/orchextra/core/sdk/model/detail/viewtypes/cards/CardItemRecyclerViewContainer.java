package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.gigigo.baserecycleradapter.adapter.BaseRecyclerAdapter;
import com.gigigo.orchextra.core.domain.entities.article.ArticleElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleRichTextElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleVideoElement;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.CardImageViewHolder;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.CardRichTextViewHolder;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.CardVideoViewHolder;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;
import java.util.List;

public class CardItemRecyclerViewContainer extends RecyclerView {

  private final Context context;

  private ImageLoader imageLoader;
  private List<ArticleElement> elements;
  private BaseRecyclerAdapter adapter;
  private int heightDevice;
  private LinearLayoutManager layoutManager;

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
    CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT);
    lp.setBehavior(new AppBarLayout.ScrollingViewBehavior());
    setLayoutParams(lp);

    heightDevice = DeviceUtils.calculateRealHeightDevice(context);

    setHasFixedSize(true);

    layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
    setLayoutManager(layoutManager);

    CardViewHolderFactory factory = new CardViewHolderFactory(context, imageLoader);

    adapter = new BaseRecyclerAdapter(factory);
    adapter.bind(ArticleImageElement.class, CardImageViewHolder.class);
    adapter.bind(ArticleRichTextElement.class, CardRichTextViewHolder.class);
    adapter.bind(ArticleVideoElement.class, CardVideoViewHolder.class);

    setAdapter(adapter);
    setItemsToAdapter();

    NestedScrollView nestedScrollView = (NestedScrollView) ((Activity) context).findViewById(R.id.nestedScrollView);
    if (nestedScrollView != null) {
      nestedScrollView.setOnScrollChangeListener(onScrollChangeListener);
    }
  }


  private NestedScrollView.OnScrollChangeListener onScrollChangeListener =
      new NestedScrollView.OnScrollChangeListener() {
        @Override
        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX,
            int oldScrollY) {
          Log.i("SCROLL", "Y: " + scrollY + " oldY: " + oldScrollY);

          int currentScrollPerPage = scrollY % heightDevice;
          int currentPage = scrollY / heightDevice;

          if (heightDevice - heightDevice / 4 < currentScrollPerPage) {
            //layoutManager.scrollToPositionWithOffset(heightDevice * 2, 2);
            //v.scrollBy(0, heightDevice * 2);
          }

          CardItemRecyclerViewContainer.this.scrollTo(0, heightDevice);

          //AppBarLayout appbarLayout = (AppBarLayout) ((Activity) context).findViewById(R.id.appbarLayout);
          //appbarLayout.setExpanded(false, false);
        }
      };

  public void setImageLoader(ImageLoader imageLoader) {
    this.imageLoader = imageLoader;
  }

  public void addCards(List<ArticleElement> elements) {
    this.elements = elements;
    setItemsToAdapter();
  }

  private void setItemsToAdapter() {
    if (adapter != null && elements != null) {
      adapter.addAll(elements);
    }
  }
}
