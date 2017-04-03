package com.gigigo.orchextra.core.sdk;

import com.gigigo.orchextra.core.controller.OcmViewGenerator;
import com.gigigo.orchextra.core.domain.entities.elementcache.cards.ElementCachePreviewCard;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.DeepLinkContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.CardContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.PreviewCardContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube.YoutubeContentData;
import com.gigigo.orchextra.ocm.callbacks.OnRetrieveUiMenuListener;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.orchextra.core.controller.model.detail.DetailElementsViewPresenter;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.ocm.views.UiDetailBaseContentData;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import com.gigigo.orchextra.ocm.views.UiSearchBaseContentData;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.OnRetrieveMenuListener;
import com.gigigo.orchextra.core.domain.entities.article.ArticleElement;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCachePreview;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheRender;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheShare;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheType;
import com.gigigo.orchextra.core.domain.entities.elements.Element;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import com.gigigo.orchextra.core.sdk.application.OcmContextProvider;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.ArticleContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.BrowserContentData;
import com.gigigo.orchextra.core.sdk.model.detail.layouts.DetailLayoutContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.PreviewContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.ScanContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.VuforiaContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.WebViewContentData;
import com.gigigo.orchextra.core.sdk.model.grid.ContentGridLayoutView;
import com.gigigo.orchextra.core.sdk.model.searcher.SearcherLayoutView;
import com.gigigo.ui.imageloader.ImageLoader;
import java.util.ArrayList;
import java.util.List;
import orchextra.javax.inject.Provider;

public class OcmViewGeneratorImp implements OcmViewGenerator {

  private final OcmController ocmController;
  private final Provider<DetailElementsViewPresenter> detailElementsViewPresenterProvider;
  private final ImageLoader imageLoader;

  private OnRetrieveUiMenuListener onRetrieveUiMenuListener;

  OnRetrieveMenuListener onRetrieveMenuListener = new OnRetrieveMenuListener() {
    @Override public void onResult(MenuContentData menuContentData) {
      List<UiMenu> menuList = new ArrayList<>();

      if (menuContentData != null
          && menuContentData.getMenuContentList() != null
          && menuContentData.getMenuContentList().size() > 0) {
        for (Element element : menuContentData.getMenuContentList().get(0).getElements()) {
          UiMenu uiMenu = new UiMenu();

          uiMenu.setSlug(element.getSlug());
          uiMenu.setText(element.getSectionView().getText());
          uiMenu.setElementUrl(element.getElementUrl());

          menuList.add(uiMenu);
        }
      }

      if (onRetrieveUiMenuListener != null) {
        onRetrieveUiMenuListener.onResult(menuList);
      }
    }

    @Override public void onNoNetworkConnectionError() {
      if (onRetrieveUiMenuListener != null) {
        onRetrieveUiMenuListener.onNoNetworkConnectionError();
      }
    }

    @Override public void onResponseDataError() {
      if (onRetrieveUiMenuListener != null) {
        onRetrieveUiMenuListener.onResponseDataError();
      }
    }
  };

  public OcmViewGeneratorImp(OcmController ocmController,
      Provider<DetailElementsViewPresenter> detailElementsViewPresenterProvider,
      ImageLoader imageLoader) {
    this.ocmController = ocmController;
    this.detailElementsViewPresenterProvider = detailElementsViewPresenterProvider;
    this.imageLoader = imageLoader;
  }

  public void getMenu(OnRetrieveUiMenuListener onRetrieveUiMenuListener) {
    this.onRetrieveUiMenuListener = onRetrieveUiMenuListener;
    ocmController.getMenu(false, onRetrieveMenuListener);
  }

  @Override public UiGridBaseContentData generateGridView(String viewId, String filter) {
    ContentGridLayoutView contentGridLayoutView = ContentGridLayoutView.newInstance();
    contentGridLayoutView.setViewId(viewId);
    contentGridLayoutView.setEmotion(filter);
    return contentGridLayoutView;
  }

  @Override public UiSearchBaseContentData generateSearchView() {
    return SearcherLayoutView.newInstance();
  }

  @Override public UiDetailBaseContentData generateDetailView(String elementUrl) {
    DetailLayoutContentData detailLayoutContentData = DetailLayoutContentData.newInstance();
    detailLayoutContentData.setPresenter(detailElementsViewPresenterProvider.get());
    detailLayoutContentData.setElementUrl(elementUrl);
    return detailLayoutContentData;
  }

  @Override
  public UiBaseContentData generateCardPreview(ElementCachePreview preview, ElementCacheShare share) {
    ElementCachePreviewCard previewCard = new ElementCachePreviewCard();
    List<ElementCachePreview> list = new ArrayList<>();
    list.add(preview);
    list.add(preview);
    list.add(preview);
    list.add(preview);
    previewCard.setPreviewList(list);

    PreviewCardContentData previewCardContentData = PreviewCardContentData.newInstance();
    previewCardContentData.setImageLoader(imageLoader);
    previewCardContentData.setPreview(previewCard);
    previewCardContentData.setShare(share);
    return previewCardContentData;
  }

  @Override
  public UiBaseContentData generatePreview(ElementCachePreview preview, ElementCacheShare share) {
    PreviewContentData previewContentData = PreviewContentData.newInstance();
    previewContentData.setImageLoader(imageLoader);
    previewContentData.setPreview(preview);
    previewContentData.setShare(share);
    return previewContentData;
  }

  @Override
  public UiBaseContentData generateDetailView(ElementCacheType type, ElementCacheRender render) {
    switch (type) {
      case ARTICLE:
        if (render != null) {
          return generateArticleDetailView(render.getElements());
        }
      case WEBVIEW:
        if (render != null) {
          return generateWebViewDetailView(render.getUrl());
        }
      case VUFORIA:
        if (render != null) {
          return generateVuforiaDetailView();
        }
      case SCAN:
        if (render != null) {
          return generateScanDetailView();
        }
      case BROWSER:
        if (render != null) {
          return generateBrowserDetailView(render.getUrl());
        }
      case VIDEO:
        if (render != null) {
          return generateYoutubeDetailView(render.getSource());
        }
      case DEEP_LINK:
        if (render != null) {
          return generateDeepLinkView(render.getUri());
        }
    }
    return null;
  }

  @Override public String getImageUrl(String elementUrl) {
    ElementCache cachedElement = ocmController.getCachedElement(elementUrl);

    if (cachedElement != null && cachedElement.getPreview() != null) {
      return cachedElement.getPreview().getImageUrl();
    }

    return null;
  }

  private UiBaseContentData generateArticleDetailView(List<ArticleElement> elements) {
    ArticleContentData articleContentData =
        ArticleContentData.newInstance();
    articleContentData.setImageLoader(imageLoader);
    articleContentData.addItems(elements);
    return articleContentData;
  }

  @Override
  public UiBaseContentData generateCardDetailView(ElementCache elements) {
    CardContentData cardContentData =
        CardContentData.newInstance();
    cardContentData.setImageLoader(imageLoader);
    cardContentData.addItems(elements);
    return cardContentData;
  }

  private UiBaseContentData generateWebViewDetailView(String url) {
    return WebViewContentData.newInstance(url);
  }

  private UiBaseContentData generateVuforiaDetailView() {
    return VuforiaContentData.newInstance();
  }

  private UiBaseContentData generateScanDetailView() {
    return ScanContentData.newInstance();
  }

  private UiBaseContentData generateBrowserDetailView(String url) {
    return BrowserContentData.newInstance(url);
  }

  private UiBaseContentData generateYoutubeDetailView(String url) {
    return YoutubeContentData.newInstance(url);
  }

  private UiBaseContentData generateDeepLinkView(String uri) {
    return DeepLinkContentData.newInstance(uri);
  }
}
