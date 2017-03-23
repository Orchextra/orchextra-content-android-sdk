package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.gigigo.orchextra.core.domain.entities.article.ArticleRichTextElement;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.ocmsdk.R;

public class CardRichTextViewHolder extends CardViewElement<ArticleRichTextElement> {

  private TextView cardRichText;
  private ArticleRichTextElement richTextElement;

  public static CardRichTextViewHolder newInstance() {
    return new CardRichTextViewHolder();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.view_card_rich_text_item, container, false);

    initViews(view);

    return view;
  }

  private void initViews(View view) {
    cardRichText = (TextView) view.findViewById(R.id.card_rich_text);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    if (richTextElement != null) {
      bindTo();
    }
  }

  private void bindTo() {
    if (!TextUtils.isEmpty(richTextElement.getHtml())) {
      cardRichText.setText(Html.fromHtml(richTextElement.getHtml()));
    }

    //int heightDevice = DeviceUtils.calculateRealHeightDevice(getContext());
    //FrameLayout.LayoutParams lp =
    //    new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightDevice);
    //cardRichText.setLayoutParams(lp);
  }

  public void setRichTextElement(ArticleRichTextElement richTextElement) {
    this.richTextElement = richTextElement;
  }
}
