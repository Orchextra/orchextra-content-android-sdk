package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders;

import android.content.Context;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.orchextra.core.data.api.utils.ConnectionUtilsImp;
import com.gigigo.orchextra.core.domain.entities.article.ArticleRichTextElement;
import com.gigigo.orchextra.core.domain.utils.ConnectionUtils;
import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;
import com.gigigo.orchextra.core.sdk.utils.DeviceUtils;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocmsdk.R;

public class ArticleRichTextView extends BaseViewHolder<ArticleRichTextElement> {

  private TextView articleRichText;
  private OcmContextProvider ocmContextProvider;
  private ConnectionUtils connectionUtils;

  public ArticleRichTextView(Context context, ViewGroup parent) {
    super(context, parent, R.layout.view_article_rich_text_item);

    connectionUtils = new ConnectionUtilsImp(context);

    articleRichText = itemView.findViewById(R.id.article_rich_text);
    ocmContextProvider = OCManager.getOcmContextProvider();
  }

  private void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
    int start = strBuilder.getSpanStart(span);
    int end = strBuilder.getSpanEnd(span);
    int flags = strBuilder.getSpanFlags(span);
    ClickableSpan clickable = new ClickableSpan() {
      public void onClick(View view) {
        if (connectionUtils.hasConnection()) {
          DeviceUtils.openChromeTabs(ocmContextProvider.getCurrentActivity(), span.getURL());
        } else {
          View rootView = ((ViewGroup) ocmContextProvider.getCurrentActivity()
              .findViewById(android.R.id.content)).getChildAt(0);

          OCManager.getCustomTranslation(R.string.oc_error_content_not_available_without_internet,
              text -> {

                if (text != null) {
                  Snackbar.make(rootView, text, Snackbar.LENGTH_SHORT).show();
                } else {
                  Snackbar.make(rootView, R.string.oc_error_content_not_available_without_internet,
                      Snackbar.LENGTH_SHORT).show();
                }
                return null;
              });
        }
      }
    };

    strBuilder.setSpan(clickable, start, end, flags);
    strBuilder.removeSpan(span);
  }

  private void setTextViewHTML(TextView text, String html) {
    CharSequence sequence = Html.fromHtml(html);
    SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
    URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
    for (URLSpan span : urls) {
      makeLinkClickable(strBuilder, span);
    }
    text.setText(strBuilder);
    text.setMovementMethod(LinkMovementMethod.getInstance());
  }

  @Override
  public void bindTo(@NonNull final ArticleRichTextElement richTextElement, int position) {

    final String htmlText =
        richTextElement.getRender() != null ? richTextElement.getRender().getHtml() : "";

    Handler handler = new Handler();
    handler.postDelayed(() -> setTextViewHTML(articleRichText, htmlText), 100);
  }
}