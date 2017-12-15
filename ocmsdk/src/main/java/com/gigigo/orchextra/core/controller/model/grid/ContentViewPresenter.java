package com.gigigo.orchextra.core.controller.model.grid;

import android.text.TextUtils;
import android.view.View;
import com.gigigo.multiplegridrecyclerview.entities.Cell;
import com.gigigo.multiplegridrecyclerview.entities.CellBlankElement;
import com.gigigo.orchextra.core.controller.dto.CellCarouselContentData;
import com.gigigo.orchextra.core.controller.dto.CellGridContentData;
import com.gigigo.orchextra.core.controller.model.base.Presenter;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItem;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItemPattern;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCacheType;
import com.gigigo.orchextra.core.domain.entities.elements.Element;
import com.gigigo.orchextra.core.domain.entities.menus.DataRequest;
import com.gigigo.orchextra.core.domain.entities.menus.RequiredAuthoritation;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import com.gigigo.orchextra.core.sdk.ui.OcmWebViewActivity;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.OcmEvent;
import com.gigigo.orchextra.ocm.dto.UiMenu;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ContentViewPresenter extends Presenter<ContentView> {

  private final Authoritation authoritation;
  private final OcmController ocmController;

  private UiMenu uiMenu;
  private int imagesToDownload = 21;
  private String filter;
  private List<Cell> listedCellContentDataList;

  private boolean hasToCheckNewContent = false;
  private int padding;

  public ContentViewPresenter(OcmController ocmController, Authoritation authoritation) {

    this.ocmController = ocmController;
    this.authoritation = authoritation;
  }

  @Override public void onViewAttached() {
    getView().initUi();
  }

  public void setHasToCheckNewContent(boolean hasToCheckNewContent) {
    this.hasToCheckNewContent = hasToCheckNewContent;
  }

  public void loadSection() {
    getView().showProgressView(true);

    loadSection(false, uiMenu, filter, false);
  }

  public void loadSection(UiMenu uiMenu) {
    loadSection(false, uiMenu, filter, false);
  }

  public void loadSection(boolean forceReload) {
    loadSection(forceReload, uiMenu, filter,false);
  }

  public void loadSection(UiMenu uiMenu, String filter) {
    loadSection(false, uiMenu, filter, false);
  }

  public void loadSectionAndNotifyMenu() {
    loadSection(true, uiMenu, filter, true);
  }

  public void loadSection(boolean forceReload, UiMenu uiMenu, String filter, boolean notifyMenu) {
    this.uiMenu = uiMenu;
    this.filter = filter;

    String contentUrl = uiMenu.getElementCache().getRender().getContentUrl();

    if (OCManager.hasOnChangedMenuCallback() && notifyMenu) {
      ocmController.refreshMenuData();
    }

    /**
     * Init app
     * Get from cache == Force = false
     * If section is null, then request from cloud and show content
     * else request from cloud, check version and checkNewContent
     *
     * Pull to refresh
     * force = true
     * Get section from cloud, if data changes refresh content
     */
    if (!forceReload) {
      ocmController.getSection(DataRequest.ONLY_CACHE, contentUrl, imagesToDownload,
          new OcmController.GetSectionControllerCallback() {

            @Override public void onGetSectionLoaded(ContentData cachedContentData) {
              if (cachedContentData == null) {
                ocmController.getSection(DataRequest.FORCE_CLOUD, contentUrl, imagesToDownload,
                    new OcmController.GetSectionControllerCallback() {

                      @Override public void onGetSectionLoaded(ContentData newContentData) {
                        renderContentItem(newContentData.getContent());
                      }

                      @Override public void onGetSectionFails(Exception e) {
                        renderError();
                      }
                    });

                return;
              } else {

                renderContentItem(cachedContentData.getContent());
                ocmController.getSection(DataRequest.FIRST_CACHE, contentUrl, imagesToDownload, new OcmController.GetSectionControllerCallback() {

                  @Override public void onGetSectionLoaded(ContentData newContentData) {
                    if (newContentData.isFromCloud()) {
                      checkNewContent(cachedContentData, newContentData);
                    }
                  }

                  @Override public void onGetSectionFails(Exception e) {

                  }
                });

                //if (cachedContentData == null) {
                //  cachedContentData = contentData;
                //
                //  ContentItem contentItem = contentData.getContent();
                //  renderContentItem(contentItem);
                //
                //  if (contentData.isFromCloud()) {
                //    checkNewContent(cachedContentData, contentData);
                //  }
                //} else if (!hasToCheckNewContent || forceReload) {
                //  cachedContentData = contentData;
                //  ContentItem contentItem = contentData.getContent();
                //  renderContentItem(contentItem);
                //  hasToCheckNewContent = true;
                //} else {
                //  checkNewContent(cachedContentData, contentData);
                //}
                //
                //cachedContentData = contentData;
              }

              OCManager.notifyOnLoadDataContentSectionFinished(uiMenu);
            }

            @Override public void onGetSectionFails(Exception e) {
              renderError();
            }
          });
    } else {
      ocmController.getSection(DataRequest.FIRST_CACHE, contentUrl, imagesToDownload,
          new OcmController.GetSectionControllerCallback() {

            @Override public void onGetSectionLoaded(ContentData contentData) {
              if (contentData.isFromCloud()) {
                renderContentItem(contentData.getContent());
              }
            }

            @Override public void onGetSectionFails(Exception e) {
              renderError();
            }
          });
    }
  }

  private void checkNewContent(ContentData cachedContentData, ContentData newContentData) {
    if (cachedContentData == null
        || newContentData == null
        || cachedContentData.getContent() == null
        || newContentData.getContent() == null
        || cachedContentData.getContent().getElements() == null
        || newContentData.getContent().getElements() == null
        || getView() == null) {
      return;
    }

    UpdateAtType updateAtType = checkDifferents(cachedContentData, newContentData);
    switch (updateAtType) {
      case NEW_CONTENT:
        getView().showNewExistingContent();
        hasToCheckNewContent = false;
        break;
      case REFRESH:
        //loadSection();
        getView().showNewExistingContent();
        hasToCheckNewContent = false;
        break;
    }

    getView().showProgressView(false);
  }

  private UpdateAtType checkDifferents(ContentData cachedContentData, ContentData newContentData) {
    List<Element> cachedElements = cachedContentData.getContent().getElements();
    List<Element> newElements = newContentData.getContent().getElements();

    if (cachedElements.size() > newElements.size()) {
      return UpdateAtType.REFRESH;
    } else if (cachedElements.size() < newElements.size()) {
      return UpdateAtType.NEW_CONTENT;
    } else {
      for (int i = 0; i < cachedElements.size(); i++) {
        if (!cachedElements.get(i).getSlug().equalsIgnoreCase(newElements.get(i).getSlug())) {
          return UpdateAtType.REFRESH;
        } else {
          ElementCache cachedElementCache =
              cachedContentData.getElementsCache().get(cachedElements.get(i).getElementUrl());

          ElementCache newElementCache =
              newContentData.getElementsCache().get(newElements.get(i).getElementUrl());

          if (cachedElementCache != null
              && newElementCache != null
              && cachedElementCache.getUpdateAt() != newElementCache.getUpdateAt()) {
            return UpdateAtType.REFRESH;
          }
        }
      }
    }
    return UpdateAtType.NONE;
  }

  private void renderContentItem(ContentItem contentItem) {
    if (getView() != null) {
      if (contentItem != null
          && contentItem.getLayout() != null
          && contentItem.getElements() != null) {
        //&& hasFilter(contentItem)) {

        listedCellContentDataList = checkTypeAndCalculateCelListedContent(contentItem);

        if (listedCellContentDataList.size() != 0) {
          getView().setData(listedCellContentDataList, contentItem.getLayout().getType());
          getView().showEmptyView(false);
          getView().showErrorView(false);
        } else {
          getView().showEmptyView(true);
        }
      } else {
        getView().showEmptyView(true);
      }

      getView().showProgressView(false);
    }
  }

  //private boolean hasFilter(ContentItem contentItem){
  //  if(filter!=null && !filter.isEmpty()){
  //    if(contentItem.getTags()!=null){
  //      return contentItem.getTags().contains(filter);
  //    }
  //    return false;
  //  }
  //  return true;
  //}

  private void renderError() {
    if (getView() != null) {
      getView().showProgressView(false);
      if (listedCellContentDataList == null || listedCellContentDataList.size() == 0) {
        getView().showErrorView(true);
      }
    }
  }

  private List<Cell> checkTypeAndCalculateCelListedContent(ContentItem contentItem) {
    switch (contentItem.getLayout().getType()) {
      case CAROUSEL:
        return calculateCarouselCells(contentItem);
      case GRID:
      default:
        return calculateGridCells(contentItem);
    }
  }

  private List<Cell> calculateCarouselCells(ContentItem contentItem) {
    List<Element> elements = contentItem.getElements();

    List<Cell> cellGridContentDataList = new ArrayList<>();

    for (int i = 0; i < elements.size(); i++) {
      Element element = elements.get(i);

      if (TextUtils.isEmpty(filter) || element.getTags().contains(filter)) {

        CellCarouselContentData cell = new CellCarouselContentData();
        cell.setData(element);
        cell.setColumn(1);
        cell.setRow(1);

        cellGridContentDataList.add(cell);
      }
    }

    return cellGridContentDataList;
  }

  private List<Cell> calculateGridCells(ContentItem contentItem) {
    int indexPattern = 0;
    List<ContentItemPattern> pattern = contentItem.getLayout().getPattern();

    List<Element> elements = contentItem.getElements();

    List<Cell> cellGridContentDataList = new ArrayList<>();

    int auxPadding = padding == 0 ? 1 : padding;

    for (int i = 0; i < elements.size(); i++) {
      Element element = elements.get(i);

      if (TextUtils.isEmpty(filter) || element.getTags().contains(filter)) {

        CellGridContentData cell = new CellGridContentData();
        cell.setData(element);
        cell.setColumn(pattern.get(indexPattern).getColumn() * auxPadding);
        cell.setRow(pattern.get(indexPattern).getRow() * auxPadding);

        indexPattern = ++indexPattern % pattern.size();

        cellGridContentDataList.add(cell);
      }
    }

    while (cellGridContentDataList.size() % 3 != 0) {
      CellBlankElement cellBlankElement = new CellBlankElement();
      cellBlankElement.setColumn(1 * auxPadding);
      cellBlankElement.setRow(1 * auxPadding);
      cellGridContentDataList.add(cellBlankElement);

      indexPattern = ++indexPattern % pattern.size();
    }

    //TODO Resolve clip to padding flashing when last row is 3 items 1x1. Remove * 2 multiplier above
    if (cellGridContentDataList.size() > 0) {
      for (int i = 0; i < 3 * 2 * padding; i++) {
        CellBlankElement cellElement = new CellBlankElement();
        cellElement.setRow(1);
        cellElement.setColumn(1);
        cellGridContentDataList.add(cellElement);
      }
    }

    return cellGridContentDataList;
  }

  public void onItemClicked(int position, View view) {
    if (position < listedCellContentDataList.size()) {

      Element element = (Element) listedCellContentDataList.get(position).getData();

      if (element == null) {
        return;
      }

      if (checkElementNeedAuthUser(element)) {
        getView().showAuthDialog(element.getElementUrl());
        return;
      }

      WeakReference<View> viewWeakReference = new WeakReference<>(view);

      ocmController.getDetails(element.getElementUrl(),
          new OcmController.GetDetailControllerCallback() {
            @Override public void onGetDetailLoaded(ElementCache elementCache) {
              String imageUrlToExpandInPreview = null;
              if (elementCache != null && elementCache.getPreview() != null) {
                imageUrlToExpandInPreview = elementCache.getPreview().getImageUrl();
              }

              if (getView() != null) {
                OCManager.notifyEvent(OcmEvent.CELL_CLICKED, elementCache);
                OCManager.addArticleToReadedArticles(element.getSlug());
                System.out.println("CELL_CLICKED: " + element.getSlug());

                if (elementCache.getType()
                    != ElementCacheType.WEBVIEW)//|| imageUrlToExpandInPreview!="" )
                {
                  getView().navigateToDetailView(element.getElementUrl(), imageUrlToExpandInPreview,
                      viewWeakReference.get());
                  //getView().navigateToDetailView(elementCache, viewWeakReference.get());
                } else {
                  OcmWebViewActivity.open(viewWeakReference.get().getContext(),
                      elementCache.getRender(), elementCache.getName());
                  //  if (imageUrlToExpandInPreview != "") {
                  //    OcmWebViewActivity.open(viewWeakReference.get().getContext(),
                  //        elementCache.getRender().getUrl(),elementCache.getName());
                  //  } else {
                  //    OcmWebViewActivity.open(viewWeakReference.get().getContext(),
                  //        elementCache.getRender().getUrl(),elementCache.getName(),imageUrlToExpandInPreview);
                  //  }
                }
              }
            }

            @Override public void onGetDetailFails(Exception e) {
              e.printStackTrace();
            }

            @Override public void onGetDetailNoAvailable(Exception e) {
              e.printStackTrace();
              getView().contentNotAvailable();
            }
          });
    }
  }

  private boolean checkElementNeedAuthUser(Element element) {
    return !checkUserHasPermissionsToShowElement(element.getSegmentation().getRequiredAuth());
  }

  private boolean checkUserHasPermissionsToShowElement(
      RequiredAuthoritation requiredAuthoritation) {
    return authoritation.isAuthorizatedUser() || !requiredAuthoritation.equals(
        RequiredAuthoritation.LOGGED);
  }

  public void setFilter(String filter) {
    this.filter = filter;
    if (getView() != null) {
      loadSection();
    }
  }

  public void setPadding(int padding) {
    this.padding = padding;
  }

  public int getChildCount() {
    return listedCellContentDataList != null ? listedCellContentDataList.size() : 0;
  }

  @Override public void detachView() {
    super.detachView();
  }

  public void setImagesToDownload(int imagesToDownload) {
    this.imagesToDownload = imagesToDownload;
  }

  public void destroy() {
    ocmController.disposeUseCases();
  }
}
