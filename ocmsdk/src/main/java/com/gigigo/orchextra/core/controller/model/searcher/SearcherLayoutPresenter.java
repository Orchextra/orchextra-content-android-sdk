package com.gigigo.orchextra.core.controller.model.searcher;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.gigigo.multiplegridrecyclerview.entities.Cell;
import com.gigigo.multiplegridrecyclerview.entities.CellBlankElement;
import com.gigigo.orchextra.core.controller.dto.CellGridContentData;
import com.gigigo.orchextra.core.controller.model.base.Presenter;
import com.gigigo.orchextra.core.domain.OcmController;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItemPattern;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.elements.Element;
import com.gigigo.orchextra.core.domain.entities.menus.RequiredAuthoritation;
import com.gigigo.orchextra.core.domain.entities.ocm.Authoritation;
import com.gigigo.orchextra.core.sdk.OcmSchemeHandler;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.OcmEvent;
import com.gigigo.orchextra.ocm.customProperties.ViewType;
import com.gigigo.orchextra.ocmsdk.R;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SearcherLayoutPresenter extends Presenter<SearcherLayoutInterface> {

  private final Authoritation authoritation;
  private final OcmController ocmController;

  private String textToSearch;
  private List<Cell> cellGridContentDataList;

  public SearcherLayoutPresenter(OcmController ocmController, Authoritation authoritation) {

    this.ocmController = ocmController;
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

    if (getView() != null) {
      if (TextUtils.isEmpty(textToSearch)) {
        getView().showEmptyView(false);
        getView().setData(new ArrayList<>());
      } else {
        sendSearch(textToSearch);
      }
    }
  }

  private void sendSearch(String textToSearch) {
    getView().showProgressView(true);

    ocmController.search(textToSearch, new OcmController.SearchControllerCallback() {
      @Override public void onSearchLoaded(ContentData contentData) {
        processResponse(contentData);
      }

      @Override public void onSearchFails(Exception e) {
        showEmptyView();
        e.printStackTrace();
      }
    });
  }

  private void showEmptyView() {
    if (getView() != null) {
      getView().showProgressView(false);
      getView().showEmptyView(true);
    }
  }

  private void processResponse(ContentData response) {
    if (getView() != null) {
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
      cell.setColumn(pattern.get(indexPattern).getColumn());
      cell.setRow(pattern.get(indexPattern).getRow());

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

      if (element == null) {
        return;
      }

      itemClickedContinue(element, activity, view);
    }
  }

  private void itemClickedContinue(Element element, AppCompatActivity activity, View view) {
    ImageView imageViewToExpandInDetail = null;

    if (view != null) {
      imageViewToExpandInDetail = (ImageView) view.findViewById(R.id.image_to_expand_in_detail);
    }

    OCManager.processElementUrl(element.getElementUrl(), imageViewToExpandInDetail, new OcmSchemeHandler.ProcessElementCallback() {
      @Override public void onProcessElementSuccess(ElementCache elementCache) {

        System.out.println("CELL_CLICKED: " + element.getSlug());
      }

      @Override public void onProcessElementFail(Exception exception) {
        exception.printStackTrace();
      }
    });
  }

  private boolean checkLoginAuth(@NonNull RequiredAuthoritation requiredAuth) {
    return authoritation.isAuthorizatedUser() || !requiredAuth.equals(RequiredAuthoritation.LOGGED);
  }

  public void updateUi() {
    doSearch();
  }

  public void destroy() {
    ocmController.disposeUseCases();
  }
}
