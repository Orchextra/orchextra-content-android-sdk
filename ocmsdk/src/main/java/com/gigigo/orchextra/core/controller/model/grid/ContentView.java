package com.gigigo.orchextra.core.controller.model.grid;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.gigigo.multiplegridrecyclerview.entities.Cell;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItemTypeLayout;
import java.util.List;

public interface ContentView {

  void initUi();

  void setData(List<Cell> cellGridContentDataList, ContentItemTypeLayout type);

  void showEmptyView(boolean isVisible);

  void showErrorView(boolean isVisible);

  void navigateToDetailView(String elementUrl, String imageToExpand, View view);

  void showAuthDialog();

  void showProgressView(boolean isVisible);

  void showNewExistingContent();

  void contentNotAvailable();
}
