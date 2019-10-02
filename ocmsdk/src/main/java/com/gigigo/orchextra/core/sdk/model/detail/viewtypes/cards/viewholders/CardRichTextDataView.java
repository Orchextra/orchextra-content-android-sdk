package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.viewholders;

import android.content.Context;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import com.gigigo.orchextra.core.domain.entities.article.ArticleRichTextElement;
import com.gigigo.orchextra.core.sdk.ui.views.textviews.OcmNormalTextView;
import com.gigigo.orchextra.ocmsdk.R;

public class CardRichTextDataView extends CardDataView {

  private OcmNormalTextView cardRichText;
  private ArticleRichTextElement richTextElement;

  public CardRichTextDataView(@NonNull Context context) {
    super(context);

    init();
  }

  public CardRichTextDataView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);

    init();
  }

  public CardRichTextDataView(@NonNull Context context, @Nullable AttributeSet attrs,
      @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    init();
  }

  private void init() {
    LayoutInflater inflater =
        (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.view_card_rich_text_item, this, true);

    initViews(view);
  }

  private void initViews(View view) {
    cardRichText = (OcmNormalTextView) view.findViewById(R.id.card_rich_text);
  }

  private void bindTo() {
    if (!TextUtils.isEmpty(richTextElement.getRender().getHtml())) {
      cardRichText.setText(Html.fromHtml(richTextElement.getRender().getHtml()));
    }
  }

  public void setRichTextElement(ArticleRichTextElement richTextElement) {
    this.richTextElement = richTextElement;
  }

  @Override public void initialize() {
    if (richTextElement != null) {
      bindTo();
    }
  }
}
