package com.gigigo.orchextra.core.controller.model.home.articles;

import com.gigigo.orchextra.core.controller.model.base.Presenter;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.menus.DataRequest;
import com.gigigo.orchextra.ocm.dto.UiMenu;

public class ContentArticleHomeLayoutViewPresenter extends Presenter<ArticleView> {

  private final OcmController ocmController;
  private ElementCache elementCache;
  private UiMenu uiMenu;

  public ContentArticleHomeLayoutViewPresenter(OcmController ocmController) {
    this.ocmController = ocmController;
  }

  @Override public void onViewAttached() {
    getView().initUi();
  }

  public void loadSectionFirstTime() {
    this.elementCache = uiMenu.getElementCache();
    loadSection();
  }

  public void loadSection() {
    if (elementCache != null
        && elementCache.getRender() != null
        && elementCache.getRender().getElements() != null) {

      getView().showArticle(elementCache.getRender().getElements());
      getView().showEmptyView(false);
    } else {
      getView().showEmptyView(true);
    }
  }

  public void reloadSection(boolean hasToShowNewContentButton) {

    String contentUrl = uiMenu.getElementCache().getRender().getContentUrl();

    ocmController.getSection(DataRequest.FIRST_CACHE, contentUrl, 0, new OcmController.GetSectionControllerCallback() {

      @Override public void onGetSectionLoaded(ContentData cachedContentData) {
        elementCache = cachedContentData.getElementsCache().get(contentUrl);

        getView().showEmptyView(false);

        if (hasToShowNewContentButton) {
          getView().showNewExistingContent();
        } else {
          loadSection();
        }
      }

      @Override public void onGetSectionFails(Exception e) {
        getView().showEmptyView(true);
      }
    });
  }

  public void setUiMenu(UiMenu uiMenu) {
    this.uiMenu = uiMenu;
  }
}
