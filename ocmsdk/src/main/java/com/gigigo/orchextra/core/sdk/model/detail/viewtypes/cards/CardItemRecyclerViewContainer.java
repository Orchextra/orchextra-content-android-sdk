package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.emoiluj.doubleviewpager.DoubleViewPager;
import com.emoiluj.doubleviewpager.DoubleViewPagerAdapter;
import com.gigigo.orchextra.core.domain.entities.article.ArticleElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleRichTextElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleVideoElement;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.CardImageDataView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.PreviewContentDataView;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;
import java.util.ArrayList;
import java.util.List;

public class CardItemRecyclerViewContainer extends LinearLayout {

  private final Context context;
  private ImageLoader imageLoader;
  private DoubleViewPager doubleViewPager;
  private ElementCache elements;

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
    doubleViewPager = (DoubleViewPager) view.findViewById(R.id.verticalViewPager);
  }

  public void setImageLoader(ImageLoader imageLoader) {
    this.imageLoader = imageLoader;
  }

  public void initialize() {
    if (imageLoader == null) {
      Log.e(getClass().getCanonicalName(), "ImageLoader and Fragment Manager can't be null");
      return;
    }

    ArrayList<PagerAdapter> verticalPagerAdapters = generateVerticalViewPagerAdapters();
    initViewPager(verticalPagerAdapters);
  }

  private ArrayList<PagerAdapter> generateVerticalViewPagerAdapters() {
    ArrayList<PagerAdapter> verticalAdapters = new ArrayList<>();

    List<List<ViewGroup>> contentVerticalAdapter = generateContentPerHorizontalPage();

    for (List<ViewGroup> adapter : contentVerticalAdapter) {
      verticalAdapters.add(createVerticalAdapters());
    }

    return verticalAdapters;
  }

  private VerticalPagerAdapter createVerticalAdapters() {
    return new VerticalPagerAdapter(context, imageLoader, elements);
  }

  private List<List<ViewGroup>> generateContentPerHorizontalPage() {
    ArrayList<ViewGroup> cardContentList = generateContentPerPage();

    ArrayList<List<ViewGroup>> contentVerticalAdapter = new ArrayList<>();

    PreviewContentDataView previewCardContentData = new PreviewContentDataView(context);
    previewCardContentData.setImageLoader(imageLoader);
    previewCardContentData.setPreview(elements.getPreview());
    previewCardContentData.setShare(elements.getShare());
    previewCardContentData.initialize();
    cardContentList.add(0, previewCardContentData);

    for (int i = 0; i < 4; i++) { //Sustituir por el numero de elemento de la preview

      contentVerticalAdapter.add((List<ViewGroup>) cardContentList.clone());
    }

    return contentVerticalAdapter;
  }

  private ArrayList<ViewGroup> generateContentPerPage() {
    ArrayList<ViewGroup> cardContentList = new ArrayList<>();

    for (ArticleElement articleElement : elements.getRender().getElements()) {
      ViewGroup content = createContent(articleElement);
      if (content != null) {
        cardContentList.add(content);
      }
    }

    return cardContentList;
  }

  private ViewGroup createContent(ArticleElement articleElement) {
    Class<? extends ArticleElement> valueClass = articleElement.getClass();

    if (valueClass == ArticleImageElement.class) {
      CardImageDataView cardImageViewHolder = new CardImageDataView(context);
      cardImageViewHolder.setImageLoader(imageLoader);
      cardImageViewHolder.setImageElement((ArticleImageElement) articleElement);
      cardImageViewHolder.initialize();
      return cardImageViewHolder;
    } else if (valueClass == ArticleRichTextElement.class) {
      //CardRichTextViewHolder cardRichTextViewHolder = CardRichTextViewHolder.newInstance();
      //cardRichTextViewHolder.setRichTextElement((ArticleRichTextElement) articleElement);
      //return cardRichTextViewHolder;
    } else if (valueClass == ArticleVideoElement.class) {
      //CardVideoViewHolder cardVideoViewHolder = CardVideoViewHolder.newInstance();
      //cardVideoViewHolder.setArticleElement((ArticleVideoElement) articleElement);
      //return cardVideoViewHolder;
    } else {
      return null;
    }
    return null;
  }

  private void initViewPager(ArrayList<PagerAdapter> verticalPagerAdapters) {
    DoubleViewPagerAdapter doubleViewPagerAdapter =
        new DoubleViewPagerAdapter(getContext(), verticalPagerAdapters);

    doubleViewPager.setAdapter(doubleViewPagerAdapter);
    doubleViewPagerAdapter.notifyDataSetChanged();
  }

  public void addCards(ElementCache elements) {
    this.elements = elements;
  }
}
