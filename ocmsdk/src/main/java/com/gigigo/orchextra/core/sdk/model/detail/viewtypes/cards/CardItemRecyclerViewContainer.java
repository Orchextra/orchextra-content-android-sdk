package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.core.domain.entities.article.ArticleElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleRichTextElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleVideoElement;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.CardImageViewHolder;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.CardRichTextViewHolder;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.CardVideoViewHolder;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders.CardViewElement;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;
import java.util.ArrayList;
import java.util.List;

public class CardItemRecyclerViewContainer extends LinearLayout {

  private ImageLoader imageLoader;
  private VerticalViewPager verticalViewPager;
  private FragmentManager supportFragmentManager;
  private List<ArticleElement> elementList;
  private CardItemViewPagerAdapter adapter;
  private UiBaseContentData preview;

  public CardItemRecyclerViewContainer(Context context) {
    super(context);

    init();
  }

  public CardItemRecyclerViewContainer(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);

    init();
  }

  public CardItemRecyclerViewContainer(Context context, @Nullable AttributeSet attrs,
      int defStyle) {
    super(context, attrs, defStyle);

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

  public void addCards(List<ArticleElement> elementList) {
    this.elementList = elementList;
  }

  public void setImageLoader(ImageLoader imageLoader) {
    this.imageLoader = imageLoader;
  }

  public void setSupportFragmentManager(FragmentManager supportFragmentManager) {
    this.supportFragmentManager = supportFragmentManager;
  }

  public void initialize() {
    if (imageLoader == null || supportFragmentManager == null) {
      Log.e(getClass().getCanonicalName(), "ImageLoader and Fragment Manager can't be null");
      return;
    }

    initViewPager();
    addContentToView();
  }

  private void initViewPager() {
    adapter = new CardItemViewPagerAdapter(supportFragmentManager);
    verticalViewPager.setAdapter(adapter);
    verticalViewPager.setSwipeOrientation(VERTICAL);
  }

  private void addContentToView() {
    List<UiBaseContentData> fragments = new ArrayList<>();

    if (preview != null) {
      fragments.add(preview);
    }

    if (elementList != null) {
      for (ArticleElement articleElement : elementList) {
        CardViewElement cardViewElement = createContent(articleElement);
        if (cardViewElement != null) {
          fragments.add(cardViewElement);
        }
      }
    }

    adapter.setFragments(fragments);
  }

  private CardViewElement createContent(ArticleElement articleElement) {
    Class<? extends ArticleElement> valueClass = articleElement.getClass();

    if (valueClass == ArticleImageElement.class) {
      CardImageViewHolder cardImageViewHolder = CardImageViewHolder.newInstance();
      cardImageViewHolder.setImageLoader(imageLoader);
      cardImageViewHolder.setImageElement((ArticleImageElement) articleElement);
      return cardImageViewHolder;
    } else if (valueClass == ArticleRichTextElement.class) {
      CardRichTextViewHolder cardRichTextViewHolder = CardRichTextViewHolder.newInstance();
      cardRichTextViewHolder.setRichTextElement((ArticleRichTextElement) articleElement);
      return cardRichTextViewHolder;
    } else if (valueClass == ArticleVideoElement.class) {
      CardVideoViewHolder cardVideoViewHolder = CardVideoViewHolder.newInstance();
      cardVideoViewHolder.setArticleElement((ArticleVideoElement) articleElement);
      return cardVideoViewHolder;
    } else {
      return null;
    }
  }

  public void setPreview(UiBaseContentData preview) {
    this.preview = preview;
  }
}
