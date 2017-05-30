package com.gigigo.orchextra.core.controller.model.searcher;

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
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItemPattern;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.elements.Element;
import com.gigigo.orchextra.core.domain.entities.menus.RequiredAuthoritation;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import com.gigigo.orchextra.core.domain.interactors.errors.GenericResponseDataError;
import com.gigigo.orchextra.core.domain.interactors.errors.NoNetworkConnectionError;
import com.gigigo.orchextra.core.domain.interactors.searcher.SearchTextInteractor;
import java.util.ArrayList;
import java.util.List;

public class SearcherLayoutPresenter extends Presenter<SearcherLayoutInterface> {

  private final InteractorInvoker interactorInvoker;
  private final SearchTextInteractor searchTextInteractor;
  private final Authoritation authoritation;
  private final OcmController ocmController;

  private String textToSearch;
  private List<Cell> cellGridContentDataList;

  public SearcherLayoutPresenter(GenericViewInjector viewInjector, OcmController ocmController,
      InteractorInvoker interactorInvoker, SearchTextInteractor searchTextInteractor,
      Authoritation authoritation) {
    super(viewInjector);

    this.ocmController = ocmController;
    this.interactorInvoker = interactorInvoker;
    this.searchTextInteractor = searchTextInteractor;
    this.authoritation = authoritation;
  }

  @Override public void onViewAttached() {
    getView().initUi();
  }

  public void doSearch() {
    doSearch(textToSearch);
  }

  public void doSearch(String textToSearch) {
    this.textToSearch = textToSearch;

    if (TextUtils.isEmpty(textToSearch)) {
      getView().hideEmptyView();
      getView().setData(new ArrayList<Cell>());
    } else {
      sendSearch(textToSearch);
    }
  }

  private void sendSearch(String textToSearch) {
    getView().showProgressView(true);

    searchTextInteractor.setTextToSearch(textToSearch);

    new InteractorExecution<>(searchTextInteractor).result(new InteractorResult<ContentData>() {
      @Override public void onResult(ContentData result) {
        processResponse(result);
      }
    }).error(NoNetworkConnectionError.class, new InteractorResult<NoNetworkConnectionError>() {
      @Override public void onResult(NoNetworkConnectionError result) {
        showEmptyView();
      }
    }).error(GenericResponseDataError.class, new InteractorResult<GenericResponseDataError>() {
      @Override public void onResult(GenericResponseDataError result) {
        showEmptyView();
      }
    }).execute(interactorInvoker);
  }

  private void showEmptyView() {
    getView().showProgressView(false);
    getView().showEmptyView();
  }

  private void processResponse(ContentData response) {
    getView().showProgressView(false);

    if (response != null
        && response.getContent() != null
        && response.getContent().getLayout() != null
        && response.getContent().getElements() != null) {

      cellGridContentDataList = calculateCellGridList(response);

      if (cellGridContentDataList != null && cellGridContentDataList.size() > 0) {
        getView().setData(cellGridContentDataList);
      } else {
        showEmptyView();
      }
    } else {
      showEmptyView();
    }
  }

  private List<Cell> calculateCellGridList(ContentData contentData) {
    int indexPattern = 0;
    List<ContentItemPattern> pattern = contentData.getContent().getLayout().getPattern();

    List<Element> elements = contentData.getContent().getElements();

    List<Cell> cellGridContentDataList = new ArrayList<>();

    for (int i = 0; i < elements.size(); i++) {
      Element element = elements.get(i);

      CellGridContentData cell = new CellGridContentData();
      cell.setData(element);
      cell.setColumn(pattern.get(i).getColumn());
      cell.setRow(pattern.get(i).getRow());

      indexPattern = ++indexPattern % pattern.size();

      cellGridContentDataList.add(cell);
    }

    while (cellGridContentDataList.size() % 3 != 0) {
      CellBlankElement cellBlankElement = new CellBlankElement();
      cellBlankElement.setColumn(pattern.get(indexPattern).getRow());
      cellBlankElement.setRow(pattern.get(indexPattern).getColumn());
      cellGridContentDataList.add(cellBlankElement);

      indexPattern = ++indexPattern % pattern.size();
    }

    return cellGridContentDataList;
  }

  public void onItemClicked(int position, AppCompatActivity activity, View view) {
    if (position < cellGridContentDataList.size()) {

      Element element = (Element) cellGridContentDataList.get(position).getData();


      ocmController.getDetails(false, element.getElementUrl(), new OcmController.GetDetailControllerCallback() {
        @Override public void onGetDetailLoaded(ElementCache elementCache) {
          String imageUrlToExpandInPreview = null;
          if (elementCache.getPreview() != null) {
            imageUrlToExpandInPreview = elementCache.getPreview().getImageUrl();
          }

          if (checkLoginAuth(element.getSegmentation().getRequiredAuth())) {

            getView().navigateToDetailView(element.getElementUrl(), imageUrlToExpandInPreview, activity,
                view);
          } else {
            getView().showAuthDialog();
          }
        }

        @Override public void onGetDetailFails(Exception e) {
          e.printStackTrace();
        }
      });
    }
  }

  private boolean checkLoginAuth(RequiredAuthoritation requiredAuthoritation) {
    return authoritation.isAuthorizatedUser() || !requiredAuthoritation.equals(
        RequiredAuthoritation.LOGGED);
  }

  public void updateUi() {
    doSearch();
  }
}
