package com.gigigo.orchextra.core.controller.model.searcher;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.gigigo.multiplegridrecyclerview.entities.Cell;
import java.util.List;

public interface SearcherLayoutInterface {

  void initUi();

  void showProgressView(boolean isVisible);

  void showEmptyView();

  void hideEmptyView();

  void setData(List<Cell> cellGridContentDataList);

  void navigateToDetailView(String elementUrl, String imageUrl, AppCompatActivity activity, View view);

  void showAuthDialog();
}
