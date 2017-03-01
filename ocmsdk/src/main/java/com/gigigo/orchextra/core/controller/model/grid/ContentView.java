package com.gigigo.orchextra.core.controller.model.grid;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.gigigo.multiplegridrecyclerview.entities.Cell;
import com.gigigo.threaddecoratedview.views.qualifiers.NotDecorated;
import com.gigigo.threaddecoratedview.views.qualifiers.ThreadDecoratedView;
import java.util.List;

@ThreadDecoratedView public interface ContentView {

  @NotDecorated void initUi();

  void setData(List<Cell> cellGridContentDataList);

  void showEmptyView();

  void showErrorView();

  void navigateToDetailView(String elementUrl, String imageToExpand, AppCompatActivity activity,
      View view);

  void showAuthDialog();

  void showProgressView(boolean isVisible);
}
