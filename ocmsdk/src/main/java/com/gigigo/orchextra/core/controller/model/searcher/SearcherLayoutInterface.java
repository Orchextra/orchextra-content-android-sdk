package com.gigigo.orchextra.core.controller.model.searcher;

import com.gigigo.multiplegridrecyclerview.entities.Cell;
import java.util.List;

public interface SearcherLayoutInterface {

  void initUi();

  void setData(List<Cell> cellGridContentDataList);

  void showProgressView(boolean isVisible);

  void showEmptyView(boolean isVisible);

  void showAuthDialog();
}
