package com.gigigo.orchextra.core.controller.model.home.articles;

import com.gigigo.orchextra.core.controller.model.base.Presenter;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import com.gigigo.orchextra.ocm.dto.UiMenuData;

public class ContentArticleHomeLayoutViewPresenter extends Presenter<ArticleView> {

  private final OcmController ocmController;
  private UiMenu uiMenu;

  public ContentArticleHomeLayoutViewPresenter(OcmController ocmController) {
    this.ocmController = ocmController;
  }

  @Override public void onViewAttached() {
    getView().initUi();
  }

  public void loadSectionFirstTime() {
    loadSection();
  }

  public void loadSection() {
    ElementCache elementCache = uiMenu.getElementCache();
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
    ocmController.getMenu(new OcmController.GetMenusControllerCallback() {
      @Override public void onGetMenusLoaded(UiMenuData menus) {

        if (menus == null) {
          return;
        }

        boolean isFound = false;
        for (UiMenu menu : menus.getUiMenuList()) {
          if (menu.getSlug().equals(uiMenu.getSlug())) {
            uiMenu = menu;
            isFound = true;
            break;
          }
        }

        if (!isFound) {
          return;
        }

        getView().showEmptyView(false);

        if (hasToShowNewContentButton) {
          getView().showNewExistingContent();
        } else {
          loadSection();
        }
      }

      @Override public void onGetMenusFails(Exception e) {
        getView().showEmptyView(true);
      }
    });
  }

  public void setUiMenu(UiMenu uiMenu) {
    this.uiMenu = uiMenu;
  }
}
