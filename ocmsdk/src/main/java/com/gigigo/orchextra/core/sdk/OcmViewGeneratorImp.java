package com.gigigo.orchextra.core.sdk;

import android.support.annotation.NonNull;
import com.gigigo.orchextra.core.controller.OcmViewGenerator;
import com.gigigo.orchextra.core.controller.model.detail.DetailElementsViewPresenter;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.core.data.rxException.ApiMenuNotFoundException;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.article.base.ArticleElement;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCachePreview;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheRender;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheShare;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheType;
import com.gigigo.orchextra.core.domain.entities.elementcache.FederatedAuthorization;
import com.gigigo.orchextra.core.domain.entities.elementcache.VideoFormat;
import com.gigigo.orchextra.core.domain.entities.elementcache.cards.ElementCachePreviewCard;
import com.gigigo.orchextra.core.domain.entities.menus.DataRequest;
import com.gigigo.orchextra.core.sdk.model.detail.layouts.DetailLayoutContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.BrowserContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.CustomTabsContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.DeepLinkContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.PreviewContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.ScanContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.VuforiaContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.WebViewContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.articletype.ArticleContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.CardContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards.PreviewCardContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.vimeo.VimeoContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube.YoutubeContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube.YoutubeFragment;
import com.gigigo.orchextra.core.sdk.model.grid.ContentGridLayoutView;
import com.gigigo.orchextra.core.sdk.model.searcher.SearcherLayoutView;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.orchextra.ocm.dto.UiMenuData;
import com.gigigo.orchextra.ocm.views.UiDetailBaseContentData;
import com.gigigo.orchextra.ocm.views.UiGridBaseContentData;
import com.gigigo.orchextra.ocm.views.UiSearchBaseContentData;
import java.util.ArrayList;
import java.util.List;
import orchextra.javax.inject.Provider;

public class OcmViewGeneratorImp implements OcmViewGenerator {

  private final OcmController ocmController;
  private final Provider<DetailElementsViewPresenter> detailElementsViewPresenterProvider;

  public OcmViewGeneratorImp(OcmController ocmController,
      Provider<DetailElementsViewPresenter> detailElementsViewPresenterProvider) {
    this.ocmController = ocmController;
    this.detailElementsViewPresenterProvider = detailElementsViewPresenterProvider;
  }

  @Override public void getMenu(DataRequest menuRequest,
      final GetMenusViewGeneratorCallback getMenusViewGeneratorCallback) {
    ocmController.getMenu(menuRequest, new OcmController.GetMenusControllerCallback() {
      @Override public void onGetMenusLoaded(UiMenuData menus) {
        getMenusViewGeneratorCallback.onGetMenusLoaded(menus);
      }

      @Override public void onGetMenusFails(Exception e) {
        getMenusViewGeneratorCallback.onGetMenusFails(new ApiMenuNotFoundException(e));
      }
    });
  }

  @Override
  public void generateSectionView(UiMenu uiMenu, String filter, final int imagesToDownload,
      GetSectionViewGeneratorCallback getSectionViewGeneratorCallback) {

    ElementCache elementCache = uiMenu.getElementCache();

    if (elementCache.getType() == ElementCacheType.ARTICLE
        && elementCache.getRender() != null
        && elementCache.getRender().getElements() != null) {

      getSectionViewGeneratorCallback.onSectionViewLoaded(
          generateArticleDetailView(elementCache.getRender().getElements()));

      OCManager.notifyOnLoadDataContentSectionFinished(uiMenu);

    } else if (elementCache.getType() == ElementCacheType.VIDEO
        && elementCache.getRender() != null) {

      if (elementCache.getRender().getFormat() == VideoFormat.YOUTUBE) {
        getSectionViewGeneratorCallback.onSectionViewLoaded(
            YoutubeFragment.newInstance(elementCache.getRender().getSource()));
      } else if (elementCache.getRender().getFormat() == VideoFormat.VIMEO) {
        //TODO Return vimeo fragment
        getSectionViewGeneratorCallback.onSectionViewLoaded(
            YoutubeFragment.newInstance(elementCache.getRender().getSource()));
      }

      OCManager.notifyOnLoadDataContentSectionFinished(uiMenu);

    } else if (elementCache.getType() == ElementCacheType.WEBVIEW
        && elementCache.getRender() != null) {

      if (elementCache.getRender().getFederatedAuth() != null
          && elementCache.getRender().getFederatedAuth().getKeys() != null
          && elementCache.getRender().getFederatedAuth().getKeys().getSiteName() != null
          && elementCache.getRender().getFederatedAuth().isActive()) {

        getSectionViewGeneratorCallback.onSectionViewLoaded(
            generateWebContentDataWithFederated(elementCache.getRender()));
      } else {
        getSectionViewGeneratorCallback.onSectionViewLoaded(
            generateWebContentData(elementCache.getRender().getUrl()));
      }

      OCManager.notifyOnLoadDataContentSectionFinished(uiMenu);

    } else if (elementCache.getRender() != null) {
      getSectionViewGeneratorCallback.onSectionViewLoaded(generateGridContentData(uiMenu, imagesToDownload, filter));
    }
  }

  private UiGridBaseContentData generateWebContentData(String url) {
    return WebViewContentData.newInstance(url);
  }

  private UiGridBaseContentData generateWebContentDataWithFederated(ElementCacheRender render) {
    return WebViewContentData.newInstance(render);
  }

  @NonNull
  private UiGridBaseContentData generateGridContentData(UiMenu uiMenu, int imagesToDownload,
      String filter) {
    ContentGridLayoutView contentGridLayoutView = ContentGridLayoutView.newInstance();
    contentGridLayoutView.setViewId(uiMenu, imagesToDownload);
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

  @Override public UiBaseContentData generateCardPreview(ElementCachePreview preview,
      ElementCacheShare share) {
    ElementCachePreviewCard previewCard = new ElementCachePreviewCard();
    List<ElementCachePreview> list = new ArrayList<>();
    list.add(preview);
    previewCard.setPreviewList(list);

    PreviewCardContentData previewCardContentData = PreviewCardContentData.newInstance();
    previewCardContentData.setPreview(previewCard);
    return previewCardContentData;
  }

  @Override
  public UiBaseContentData generatePreview(ElementCachePreview preview, ElementCacheShare share) {
    PreviewContentData previewContentData = PreviewContentData.newInstance();
    previewContentData.setPreview(preview);
    return previewContentData;
  }

  @Override
  public UiBaseContentData generateDetailView(ElementCacheType type, ElementCacheRender render) {
    switch (type) {
      case ARTICLE:
        if (render != null) {
          return generateArticleDetailView(render.getElements());
        }
      case VUFORIA:
        if (render != null) {
          return generateVuforiaDetailView();
        }
      case SCAN:
        if (render != null) {
          return generateScanDetailView();
        }
      case WEBVIEW:
        if (render != null) {
          return generateWebContentDataWithFederated(render);
        }
      case BROWSER:
        if (render != null) {
          return generateCustomTabsDetailView(render.getUrl(), render.getFederatedAuth());
        }
      case EXTERNAL_BROWSER:
        if (render != null) {
          return generateBrowserDetailView(render.getUrl(), render.getFederatedAuth());
        }
      case VIDEO:
        if (render != null) {
          return generateVideoDetailView(render);
        }
      case DEEP_LINK:
        if (render != null) {
          return generateDeepLinkView(render.getUri());
        }
    }
    return null;
  }

  @Override public void getImageUrl(String elementUrl,
      final GetDetailImageViewGeneratorCallback getDetailImageViewGeneratorCallback) {
    ocmController.getDetails(elementUrl, new OcmController.GetDetailControllerCallback() {
      @Override public void onGetDetailLoaded(ElementCache elementCache) {
        if (elementCache != null && elementCache.getPreview() != null) {
          getDetailImageViewGeneratorCallback.onGetImageLoaded(
              elementCache.getPreview().getImageUrl());
        }
      }

      @Override public void onGetDetailFails(Exception e) {
        e.printStackTrace();
      }

      @Override public void onGetDetailNoAvailable(Exception e) {
        e.printStackTrace();
      }
    });
  }

  private UiGridBaseContentData generateArticleDetailView(List<ArticleElement> elements) {
    ArticleContentData articleContentData = ArticleContentData.newInstance();
    articleContentData.addItems(elements);
    return articleContentData;
  }

  @Override public UiBaseContentData generateCardDetailView(ElementCache elements) {
    CardContentData cardContentData = CardContentData.newInstance();
    cardContentData.addItems(elements);
    return cardContentData;
  }

  private UiBaseContentData generateCustomTabsDetailView(String url,
      FederatedAuthorization federatedAuthorization) {
    return CustomTabsContentData.newInstance(url, federatedAuthorization);
  }

  private UiBaseContentData generateVuforiaDetailView() {
    return VuforiaContentData.newInstance();
  }

  private UiBaseContentData generateScanDetailView() {
    return ScanContentData.newInstance();
  }

  private UiBaseContentData generateBrowserDetailView(String url,
      FederatedAuthorization federatedAuthorization) {
    return BrowserContentData.newInstance(url, federatedAuthorization);
  }

  private UiBaseContentData generateVideoDetailView(ElementCacheRender render) {
    switch (render.getFormat()) {
      case YOUTUBE:
        return generateYoutubeDetailView(render.getSource());
      case VIMEO:
        return generateVimeoDetailView(render.getSource());
      default:
        return null;
    }
  }

  private UiBaseContentData generateYoutubeDetailView(String videoId) {
    return YoutubeContentData.newInstance(videoId);
  }

  private UiBaseContentData generateVimeoDetailView(String videoId) {
    return VimeoContentData.newInstance(videoId);
  }

  private UiBaseContentData generateDeepLinkView(String uri) {
    return DeepLinkContentData.newInstance(uri);
  }
}
