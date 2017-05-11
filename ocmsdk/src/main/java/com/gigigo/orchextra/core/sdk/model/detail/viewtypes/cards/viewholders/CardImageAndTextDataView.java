package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageAndTextElement;
import com.gigigo.orchextra.ocmsdk.R;

public class CardImageAndTextDataView extends CardDataView {

  //private ConstraintLayout contraintLayoutContainer;
  private ImageView cardImagePlaceholder;
  private TextView cardRichText;
  private ArticleImageAndTextElement dataElement;
  private ITEM firstItem = ITEM.IMAGE;
  private float ratioFirstItem = 0.5f, ratioSecondItem = 0.5f;

  public CardImageAndTextDataView(@NonNull Context context) {
    super(context);

    init();
  }

  public CardImageAndTextDataView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);

    init();
  }

  public CardImageAndTextDataView(@NonNull Context context, @Nullable AttributeSet attrs,
      @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    init();
  }

  private void init() {
    LayoutInflater inflater =
        (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.view_card_image_and_text_item, this, true);

    initViews(view);
  }

  private void initViews(View view) {
    //contraintLayoutContainer = (ConstraintLayout) view.findViewById(R.id.contraintLayoutContainer);
    cardImagePlaceholder = (ImageView) view.findViewById(R.id.card_image_placeholder);
    cardRichText = (TextView) view.findViewById(R.id.card_rich_text);
  }

  public void setFirstItem(ITEM firstItem) {
    this.firstItem = firstItem;
  }

  public void setDataElement(ArticleImageAndTextElement dataElement) {
    this.dataElement = dataElement;
  }

  @Override public void initialize() {
    setConstraints();
  }

  private void setConstraints() {
    if (dataElement != null
        && dataElement.getRatios() != null
        && dataElement.getRatios().size() >= 2) {

      ratioFirstItem = dataElement.getRatios().get(0);
      ratioSecondItem = dataElement.getRatios().get(1);
    }

    if (firstItem == ITEM.IMAGE) {
      setContraintsToView(cardImagePlaceholder, cardRichText);
    } else {
      setContraintsToView(cardRichText, cardImagePlaceholder);
    }
  }

  private void setContraintsToView(View firstItem, View secondItem) {
    //ConstraintSet constraintSet = new ConstraintSet();
    //constraintSet.clone(contraintLayoutContainer);
    //
    //constraintSet.connect(firstItem.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID,
    //    ConstraintSet.TOP);
    //constraintSet.connect(firstItem.getId(), ConstraintSet.BOTTOM, secondItem.getId(),
    //    ConstraintSet.TOP);
    //
    //constraintSet.connect(secondItem.getId(), ConstraintSet.TOP, firstItem.getId(),
    //    ConstraintSet.BOTTOM);
    //constraintSet.connect(secondItem.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID,
    //    ConstraintSet.BOTTOM);
    //
    //constraintSet.setVerticalWeight(firstItem.getId(), ratioFirstItem);
    //constraintSet.setVerticalWeight(secondItem.getId(), ratioSecondItem);
    //
    //constraintSet.applyTo(contraintLayoutContainer);
  }

  public enum ITEM {
    IMAGE, TEXT
  }
}
