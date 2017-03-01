package com.gigigo.orchextra.core.controller.model.grid;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import com.gigigo.interactorexecutor.base.Presenter;
import com.gigigo.interactorexecutor.base.invoker.InteractorExecution;
import com.gigigo.interactorexecutor.base.invoker.InteractorInvoker;
import com.gigigo.interactorexecutor.base.invoker.InteractorResult;
import com.gigigo.interactorexecutor.base.viewinjector.GenericViewInjector;
import com.gigigo.multiplegridrecyclerview.entities.Cell;
import com.gigigo.multiplegridrecyclerview.entities.CellBlankElement;
import com.gigigo.orchextra.core.controller.dto.CellGridContentData;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItem;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItemPattern;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.elements.Element;
import com.gigigo.orchextra.core.domain.entities.menus.RequiredAuthoritation;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import com.gigigo.orchextra.core.domain.interactor.errors.GenericResponseDataError;
import com.gigigo.orchextra.core.domain.interactor.errors.NoNetworkConnectionError;
import com.gigigo.orchextra.core.domain.interactor.home.GetSectionDataInteractor;
import java.util.ArrayList;
import java.util.List;

public class ContentViewPresenter extends Presenter<ContentView> {

  private final InteractorInvoker interactorInvoker;
  private final GetSectionDataInteractor getHomeDataInteractor;
  private final Authoritation authoritation;
  private final OcmController ocmController;

  private String section;
  private String filter;
  private List<Cell> cellGridContentDataList;

  public ContentViewPresenter(GenericViewInjector viewInjector, OcmController ocmController,
      InteractorInvoker interactorInvoker, GetSectionDataInteractor getHomeDataInteractor,
      Authoritation authoritation) {
    super(viewInjector);

    this.ocmController = ocmController;
    this.interactorInvoker = interactorInvoker;
    this.getHomeDataInteractor = getHomeDataInteractor;
    this.authoritation = authoritation;
  }

  @Override public void onViewAttached() {
    getView().initUi();
  }

  public void loadSection(String viewId, boolean useCache, String filter) {
    this.section = viewId;
    this.filter = filter;

    loadSection(useCache);
  }

  public void reloadSection() {
    loadSection(false);
  }

  private void loadSection(final boolean useCache) {
    getView().showProgressView(true);

    getHomeDataInteractor.setSection(section);
    getHomeDataInteractor.setUseCache(useCache);

    new InteractorExecution<>(getHomeDataInteractor).result(new InteractorResult<ContentItem>() {

      @Override public void onResult(ContentItem contentItem) {
        if (contentItem != null
            && contentItem.getLayout() != null
            && contentItem.getElements() != null) {

          cellGridContentDataList = calculateCellGridList(contentItem);

          if (cellGridContentDataList.size() != 0) {
            getView().setData(cellGridContentDataList);
          } else {
            getView().showEmptyView();
          }
        } else {
          getView().showEmptyView();
        }

        getView().showProgressView(false);
      }
    }).error(NoNetworkConnectionError.class, new InteractorResult<NoNetworkConnectionError>() {
      @Override public void onResult(NoNetworkConnectionError result) {
        getView().showProgressView(false);
        getView().showErrorView();
      }
    }).error(GenericResponseDataError.class, new InteractorResult<GenericResponseDataError>() {
      @Override public void onResult(GenericResponseDataError result) {
        getView().showProgressView(false);
        getView().showErrorView();
      }
    }).execute(interactorInvoker);
  }

  private List<Cell> calculateCellGridList(ContentItem contentItem) {
    int indexPattern = 0;
    List<ContentItemPattern> pattern = contentItem.getLayout().getPattern();

    List<Element> elements = contentItem.getElements();

    List<Cell> cellGridContentDataList = new ArrayList<>();

    for (int i = 0; i < elements.size(); i++) {
      Element element = elements.get(i);

      if (TextUtils.isEmpty(filter) || element.getTags().contains(filter)) {

        CellGridContentData cell = new CellGridContentData();
        cell.setData(element);
        cell.setColumn(pattern.get(indexPattern).getRow() * 2);
        cell.setRow(pattern.get(indexPattern).getColumn() * 2);

        indexPattern = ++indexPattern % pattern.size();

        cellGridContentDataList.add(cell);
      }
    }

    while (cellGridContentDataList.size() % 3 != 0) {
      CellBlankElement cellBlankElement = new CellBlankElement();
      cellBlankElement.setColumn(pattern.get(indexPattern).getRow() * 2);
      cellBlankElement.setRow(pattern.get(indexPattern).getColumn() * 2);
      cellGridContentDataList.add(cellBlankElement);

      indexPattern = ++indexPattern % pattern.size();
    }

    //TODO Resolve clip to padding flashing when last row is 3 items 1x1. Remove * 2 multiplier above
    if (cellGridContentDataList.size() > 0) {
      for (int i = 0; i < 12; i++) {
        CellBlankElement cellElement = new CellBlankElement();
        cellElement.setRow(1);
        cellElement.setColumn(1);
        cellGridContentDataList.add(cellElement);
      }
    }

    return cellGridContentDataList;
  }

  public void onItemClicked(int position, AppCompatActivity activity, View view) {
    if (position < cellGridContentDataList.size()) {

      Element element = (Element) cellGridContentDataList.get(position).getData();

      ElementCache cachedElement = ocmController.getCachedElement(element.getElementUrl());

      String imageUrlToExpandInPreview = null;
      if (cachedElement.getPreview() != null) {
        imageUrlToExpandInPreview = cachedElement.getPreview().getImageUrl();
      }

      if (checkLoginAuth(element.getSegmentation().getRequiredAuth())) {
        getView().navigateToDetailView(element.getElementUrl(), imageUrlToExpandInPreview, activity,
            view);
      } else {
        getView().showAuthDialog();
      }
    }
  }

  private boolean checkLoginAuth(RequiredAuthoritation requiredAuthoritation) {
    return authoritation.isAuthorizatedUser() || !requiredAuthoritation.equals(
        RequiredAuthoritation.LOGGED);
  }

  public void setFilter(String filter) {
    this.filter = filter;
    if (getView() != null) loadSection(false);
  }
}
