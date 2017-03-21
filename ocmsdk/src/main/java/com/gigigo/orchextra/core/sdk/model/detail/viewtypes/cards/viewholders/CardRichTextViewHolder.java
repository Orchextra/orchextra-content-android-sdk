package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.orchextra.core.domain.entities.article.ArticleRichTextElement;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.ocmsdk.R;

public class CardRichTextViewHolder extends BaseViewHolder<ArticleRichTextElement> {

  private final Context context;
  private final TextView cardRichText;

  public CardRichTextViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.view_card_rich_text_item);
    this.context = context;

    cardRichText = (TextView) itemView.findViewById(R.id.card_rich_text);
  }

  @Override public void bindTo(ArticleRichTextElement richTextElement, int i) {
    if (!TextUtils.isEmpty(richTextElement.getHtml())) {
      cardRichText.setText(Html.fromHtml(richTextElement.getHtml()));
    }

    int heightDevice = DeviceUtils.calculateRealHeightDevice(context);
    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightDevice);
    cardRichText.setLayoutParams(lp);
  }
}
