package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.gigigo.orchextra.core.domain.entities.article.ArticleElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageAndTextElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleRichTextElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleTextAndImageElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleVideoElement;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCachePreview;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.dto.ArticleBlankElement;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.CardImageAndTextDataView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.CardImageDataView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.CardRichTextDataView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.CardVideoView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.PreviewContentDataView;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;
import java.util.ArrayList;
import views.gigigo.com.tviewpager.CallBackAdapterItemInstanciate;
import views.gigigo.com.tviewpager.CustomHorizontalPagerAdapter;
import views.gigigo.com.tviewpager.CustomVerticalPagerAdapter;
import views.gigigo.com.tviewpager.OnVHPageChangeListener;
import views.gigigo.com.tviewpager.VerticalViewPager;

public class CardItemRecyclerViewContainer extends LinearLayout {

  private final Context context;

  private ImageLoader imageLoader;
  private VerticalViewPager verticalViewPager;
  private ElementCache elements;
  private Handler handler = new Handler();
  private ArrayList<ElementCachePreview> previewList;
  private ArrayList<ArticleElement> elementCacheList;
  private int verticalPosition;
  private OnChangeVerticalPageListener onChangeVerticalPageListener;

  private Runnable switchPageRunnable = new Runnable() {
    @Override public void run() {
      verticalViewPager.nextPage(true);

      startSwitchingPageAutomatically();
    }
  };

  private CallBackAdapterItemInstanciate onInflateCustomLayoutCallback =
      new CallBackAdapterItemInstanciate() {
        @Override public Object OnVerticalInstantiateItem(ViewGroup collection, int position) {

          ArticleElement articleElement = elementCacheList.get(position);

          if (articleElement.getClass() == ArticleImageElement.class) {
            CardImageDataView cardImageViewHolder = new CardImageDataView(context);
            cardImageViewHolder.setImageLoader(imageLoader);
            cardImageViewHolder.setImageElement((ArticleImageElement) articleElement);
            cardImageViewHolder.initialize();

            return cardImageViewHolder;
          } else if (articleElement.getClass() == ArticleRichTextElement.class) {
            CardRichTextDataView cardRichTextViewHolder = new CardRichTextDataView(context);
            cardRichTextViewHolder.setRichTextElement((ArticleRichTextElement) articleElement);
            cardRichTextViewHolder.initialize();

            return cardRichTextViewHolder;
          } else if (articleElement.getClass() == ArticleVideoElement.class) {
            CardVideoView cardVideoViewHolder = new CardVideoView(context);
            cardVideoViewHolder.setArticleElement((ArticleVideoElement) articleElement);
            cardVideoViewHolder.initialize();

            return cardVideoViewHolder;
          } else if (articleElement.getClass() == ArticleImageAndTextElement.class) {
            CardImageAndTextDataView cardRichTextViewHolder = new CardImageAndTextDataView(context);
            cardRichTextViewHolder.setDataElement((ArticleImageAndTextElement) articleElement);
            cardRichTextViewHolder.setFirstItem(CardImageAndTextDataView.ITEM.IMAGE);
            cardRichTextViewHolder.initialize();

            return cardRichTextViewHolder;
          } else if (articleElement.getClass() == ArticleTextAndImageElement.class) {
            CardImageAndTextDataView cardRichTextViewHolder = new CardImageAndTextDataView(context);
            cardRichTextViewHolder.setDataElement((ArticleImageAndTextElement) articleElement);
            cardRichTextViewHolder.setFirstItem(CardImageAndTextDataView.ITEM.TEXT);
            cardRichTextViewHolder.initialize();

            return cardRichTextViewHolder;
          }

          return null;
        }

        @Override public Object OnHorizontalInstantiateItem(ViewGroup collection, int position) {
          int index = CustomHorizontalPagerAdapter.getVirtualPosition(position, previewList.size());

          ElementCachePreview elementCachePreview = previewList.get(index);

          PreviewContentDataView previewCardContentData = new PreviewContentDataView(context);
          previewCardContentData.setImageLoader(imageLoader);
          previewCardContentData.setPreview(elementCachePreview);
          previewCardContentData.setShare(elements.getShare());
          previewCardContentData.initialize();

          return previewCardContentData;
        }
      };
  private Runnable startAutoSwipingWhenPositionIsZero = new Runnable() {

    @Override public void run() {
      if (verticalPosition == 0) {
        startSwitchingPageAutomatically();
      }

      //if (onChangeVerticalPageListener != null) {
      // onChangeVerticalPageListener.onChangeVerticalPage(currentPageSelectedWhenScrolled);
      //}
    }
  };
  private OnVHPageChangeListener onChangePageListener = new OnVHPageChangeListener() {
    @Override public void onChangeHorizontalPage(int position) {
      onSwipePage();
    }

    @Override public void onChangeVerticalPage(int position) {
      verticalPosition = position;
      onSwipePage();
    }
  };

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
    LayoutInflater inflater =
        (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.view_card_item_list_layout, this, true);

    initViews(view);
  }

  private void initViews(View view) {
    verticalViewPager = (VerticalViewPager) view.findViewById(R.id.verticalViewPager);
  }

  public void setImageLoader(ImageLoader imageLoader) {
    this.imageLoader = imageLoader;
  }

  public void initialize() {
    if (imageLoader == null) {
      Log.e(getClass().getCanonicalName(), "ImageLoader and Fragment Manager can't be null");
      return;
    }

    initViewPager();

    startSwitchingPageAutomatically();
  }

  private void initViewPager() {
    CustomVerticalPagerAdapter verticalViewPagerAdapter =
        new CustomVerticalPagerAdapter(getContext(), elementCacheList, previewList,
            onInflateCustomLayoutCallback, onChangePageListener);

    verticalViewPager.setAdapter(verticalViewPagerAdapter);
  }

  public void addCards(ElementCache elements) {
    this.elements = elements;

    ArrayList<ElementCachePreview> previewList = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      previewList.add(elements.getPreview());
    }

    this.previewList = previewList;

    ArrayList<ArticleElement> elementCacheList = new ArrayList<>();
    for (ArticleElement articleElement : elements.getRender().getElements()) {
      elementCacheList.add(articleElement);
    }
    elementCacheList.add(0, new ArticleBlankElement());

    this.elementCacheList = elementCacheList;
  }

  public void startSwitchingPageAutomatically() {
    handler.postDelayed(switchPageRunnable, 5000);
  }

  public void stopSwitchingPageAutomatically() {
    handler.removeCallbacks(switchPageRunnable);
  }

  public void onSwipePage() {
    stopSwitchingPageAutomatically();

    handler.postDelayed(startAutoSwipingWhenPositionIsZero, 1000);
  }

  public void setOnChangeVerticalPageListener(
      OnChangeVerticalPageListener onChangeVerticalPageListener) {
    this.onChangeVerticalPageListener = onChangeVerticalPageListener;
  }

  public interface OnChangeVerticalPageListener {
    void onChangeVerticalPage(int numPage);
  }
}
