package com.gigigo.orchextra.core.controller.model.grid;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.gigigo.multiplegridrecyclerview.entities.Cell;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItemTypeLayout;
import java.util.List;

public interface ContentView {

  void initUi();

  void setData(List<Cell> cellGridContentDataList, ContentItemTypeLayout type);

  void showEmptyView();

  void showErrorView();

  void navigateToDetailView(String elementUrl, String imageToExpand, AppCompatActivity activity,
      View view);

  void showAuthDialog();

  void showProgressView(boolean isVisible);
}
