package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gigigo.baserecycleradapter.adapter.BaseRecyclerAdapter;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.core.domain.entities.article.ArticleButtonElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleHeaderElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleImageElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleRichTextElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleVimeoVideoElement;
import com.gigigo.orchextra.core.domain.entities.article.ArticleYoutubeVideoElement;
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement;
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElementRender;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.sdk.OcmSchemeHandler;
import com.gigigo.orchextra.core.sdk.actions.ActionHandler;
import com.gigigo.orchextra.core.sdk.di.injector.Injector;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleBlankView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleButtonView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleHeaderView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleImageView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleRichTextView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleVimeoVideoView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.ArticleYoutubeVideoView;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.viewholders.dto.ArticleBlankElement;
import com.gigigo.orchextra.core.sdk.model.grid.dto.ClipToPadding;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.Ocm;
import com.gigigo.orchextra.ocm.customProperties.ViewType;
import com.gigigo.orchextra.ocmsdk.R;

import java.util.List;

import javax.inject.Inject;

public class ArticleContentData extends UiBaseContentData {

    private List<ArticleElement<ArticleElementRender>> articleElementList;
    private RecyclerView articleItemViewContainer;
    private FrameLayout flFA;
    private View faLoading;
    private View emptyLayout;
    private BaseRecyclerAdapter<ArticleElement<ArticleElementRender>> adapter;
    private int addictionalPadding;
    private boolean thumbnailEnabled;
    @Inject
    ActionHandler actionHandler;

    public static ArticleContentData newInstance() {
        return new ArticleContentData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector injector = OCManager.getInjector();
        if (injector != null) {
            thumbnailEnabled = injector.provideOcmStyleUi().isThumbnailEnabled();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_article_elements_item, container, false);

        initDi();
        initViews(view);
        initRecyclerView();
        setClipToPaddingBottomSize(ClipToPadding.PADDING_NONE, addictionalPadding);

        return view;
    }

    private void initDi() {
        Injector injector = OCManager.getInjector();
        if (injector != null) {
            injector.injectArticleContentData(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        flFA.setVisibility(View.INVISIBLE);
        faLoading.setVisibility(View.GONE);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        if (articleItemViewContainer != null) {
            unbindDrawables(articleItemViewContainer);
            System.gc();

            Glide.get(this.getContext()).clearMemory();
            articleItemViewContainer.removeAllViews();
            Glide.get(this.getContext()).clearMemory();

            articleItemViewContainer = null;
            articleElementList = null;
        }

        if (getHost() != null) {
            super.onDestroy();
        }
    }

    private void unbindDrawables(View view) {
        System.gc();
        Runtime.getRuntime().gc();
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    private void initViews(View view) {
        articleItemViewContainer = view.findViewById(R.id.articleItemListLayout);

        flFA = view.findViewById(R.id.flFA);
        faLoading = flFA.findViewById(R.id.progressFA);
        emptyLayout = view.findViewById(R.id.ocm_empty_layout);
    }

    private void initRecyclerView() {
        ArticleContentDataFactory factory =
                new ArticleContentDataFactory(getContext(), thumbnailEnabled, actionHandler);
        adapter = new BaseRecyclerAdapter(factory);

        adapter.bind(ArticleYoutubeVideoElement.class, ArticleYoutubeVideoView.class);
        adapter.bind(ArticleVimeoVideoElement.class, ArticleVimeoVideoView.class);
        adapter.bind(ArticleRichTextElement.class, ArticleRichTextView.class);
        adapter.bind(ArticleImageElement.class, ArticleImageView.class);
        adapter.bind(ArticleHeaderElement.class, ArticleHeaderView.class);
        adapter.bind(ArticleButtonElement.class, ArticleButtonView.class);
        adapter.bind(ArticleBlankElement.class, ArticleBlankView.class);

        adapter.setMillisIntervalToAvoidDoubleClick(1500);

        adapter.setItemClickListener((i, view) -> {
            ArticleElement element = adapter.getItem(i);

            if (element instanceof ArticleButtonElement) {
                flFA.setVisibility(View.VISIBLE);
                faLoading.setVisibility(View.VISIBLE);

                String elementUrl = ((ArticleButtonElement) element).getRender().getElementUrl();

                if (elementUrl != null) {
                    if (element.getCustomProperties() != null) {
                        OCManager.notifyCustomBehaviourContinue(element.getCustomProperties(),
                                ViewType.BUTTON_ELEMENT, canContinue -> {
                                    if (canContinue) {
                                        Ocm.processElementUrl(elementUrl, null,
                                                new OcmSchemeHandler.ProcessElementCallback() {
                                                    @Override
                                                    public void onProcessElementSuccess(ElementCache elementCache) {
                                                        showErrorView(false);
                                                    }

                                                    @Override
                                                    public void onProcessElementFail(Exception exception) {
                                                        showErrorView(true);
                                                    }
                                                });
                                    }

                                    flFA.setVisibility(View.INVISIBLE);
                                    faLoading.setVisibility(View.GONE);
                                    return null;
                                });
                    } else {
                        Ocm.processElementUrl(elementUrl, null, new OcmSchemeHandler.ProcessElementCallback() {
                            @Override
                            public void onProcessElementSuccess(ElementCache elementCache) {
                                showErrorView(false);
                            }

                            @Override
                            public void onProcessElementFail(Exception exception) {

                                showErrorView(true);
                            }
                        });

                        flFA.setVisibility(View.INVISIBLE);
                        faLoading.setVisibility(View.GONE);
                    }
                }
            }
        });

        articleItemViewContainer.setAdapter(adapter);
        articleItemViewContainer.setLayoutManager(new LinearLayoutManager(getContext()));
        articleItemViewContainer.setNestedScrollingEnabled(false);
        articleItemViewContainer.setHasFixedSize(false);

        if (articleElementList != null) {
            adapter.addAll(articleElementList);
        }
    }

    public void showErrorView(boolean isVisible) {
        emptyLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        articleItemViewContainer.setVisibility(!isVisible ? View.VISIBLE : View.GONE);
    }

    public void addItems(List<ArticleElement<ArticleElementRender>> articleElementList) {
        this.articleElementList = articleElementList;
    }

    public void scrollToTop() {
        if (articleItemViewContainer != null) {
            articleItemViewContainer.scrollTo(0, 0);
        }
    }

    public void setClipToPaddingBottomSize(ClipToPadding clipToPadding, int addictionalPadding) {
        this.addictionalPadding = addictionalPadding;
        if (articleItemViewContainer != null) {
            articleItemViewContainer.setClipToPadding(false);
            articleItemViewContainer.setPadding(0, 0, 0, addictionalPadding);
        }
    }
}
