package com.gigigo.orchextra.core.controller.model.home.grid;

import com.gigigo.multiplegridrecyclerview.entities.Cell;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItemTypeLayout;
import java.util.List;

public interface ContentView {

  void initUi();

  void setData(List<Cell> cellGridContentDataList, ContentItemTypeLayout type);

  void showEmptyView(boolean isVisible);

  void showErrorView(boolean isVisible);

  void showProgressView(boolean isVisible);

  void showNewExistingContent();

  void contentNotAvailable();
}
