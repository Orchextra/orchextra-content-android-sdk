package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.gigigo.orchextra.core.domain.entities.article.ArticleElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleHeaderElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleRichTextElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleVideoElement;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleBaseView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleBlankView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleHeaderView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleImageView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleRichTextView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleVideoView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.dto.ArticleBlankElement;
import com.gigigo.orchextra.ocmsdk.R;
import com.gigigo.ui.imageloader.ImageLoader;
import java.util.List;

public class ArticleItemViewContainer extends LinearLayout {

  private final Context context;
  private LinearLayout articleListContainer;
  private ImageLoader imageLoader;

  public ArticleItemViewContainer(Context context) {
    super(context);
    this.context = context;

    init();
  }

  public ArticleItemViewContainer(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;

    init();
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public ArticleItemViewContainer(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.context = context;

    init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public ArticleItemViewContainer(Context context, AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.context = context;

    init();
  }

  private void init() {
    LayoutInflater inflater =
        (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.view_article_item_list_layout, this, true);

    articleListContainer = (LinearLayout) view.findViewById(R.id.articleListContainer);

    /*
    final ScrollView scrollView = (ScrollView) view.findViewById(R.id.articleListScroll);
    final ViewTreeObserver.OnScrollChangedListener onScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {
      @Override public void onScrollChanged() {
        Log.i("SCROLL", "getScrollY:"+scrollView.getScrollY());
        OCManager.notifyEvent(OcmEvent.CONTENT_END, null);
      }
    };
    scrollView.setOnTouchListener(new OnTouchListener() {
      private ViewTreeObserver observer;

      @Override public boolean onTouch(View v, MotionEvent event) {
        if (observer == null) {
          observer = scrollView.getViewTreeObserver();
          observer.addOnScrollChangedListener(onScrollChangedListener);
        }
        else if (!observer.isAlive()) {
          observer.removeOnScrollChangedListener(onScrollChangedListener);
          observer = scrollView.getViewTreeObserver();
          observer.addOnScrollChangedListener(onScrollChangedListener);
        }

        return false;
      }
    });
    */
  }

  public void addArticleElementList(List<ArticleElement> articleElementList) {
    if (articleElementList != null) {
      for (ArticleElement articleElement : articleElementList) {
        ArticleBaseView articleBaseView = create(articleElement);

        if (articleBaseView != null) {
          LinearLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.WRAP_CONTENT);

          articleListContainer.addView(articleBaseView, lp);
        }
      }
    }
  }
//todo change by factory
  public ArticleBaseView create(ArticleElement articleElement) {
    Class<? extends ArticleElement> valueClass = articleElement.getClass();

    if (valueClass == ArticleHeaderElement.class) {
      return new ArticleHeaderView(context, (ArticleHeaderElement) articleElement, imageLoader);
    } else if (valueClass == ArticleImageElement.class) {
      return new ArticleImageView(context, (ArticleImageElement) articleElement, imageLoader);
    } else if (valueClass == ArticleRichTextElement.class) {
      return new ArticleRichTextView(context, (ArticleRichTextElement) articleElement);
    } else if (valueClass == ArticleVideoElement.class) {
      return new ArticleVideoView(context, (ArticleVideoElement) articleElement);
    } else if (valueClass == ArticleBlankElement.class) {
      return new ArticleBlankView(context, (ArticleBlankElement) articleElement);
    } else {
      return null;
    }
  }

  public void setImageLoader(ImageLoader imageLoader) {
    this.imageLoader = imageLoader;
  }


}
