package com.gigigo.orchextra.ocm.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.gigigo.orchextra.ocm.views.UiSearchBaseContentData;

public class SearcherActivity extends AppCompatActivity {

  SearcherPresenter presenter;

  private EditText searchEditText;
  private TextView.OnEditorActionListener onClickSearchButton =
      new TextView.OnEditorActionListener() {
        @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
          if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            CharSequence searchText = v.getText();
            if (searchText != null) {
              presenter.doSearch(searchText.toString());
              hideKeyboard();
            }
            return true;
          }
          return false;
        }
      };

  public static void open(Context context) {
    Intent intent = new Intent(context, SearcherActivity.class);
    context.startActivity(intent);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_searcher_layout);

    presenter = new SearcherPresenter();
    presenter.attachView(this);
  }

  public void initUi() {
    initViews();
    setListeners();

    presenter.loadView();
  }

  private void initViews() {
    searchEditText = (EditText) findViewById(R.id.search_edit_text);
  }

  private void setListeners() {
    searchEditText.setOnEditorActionListener(onClickSearchButton);
  }

  public void setView(UiSearchBaseContentData uiSearchBaseContentData) {
    try {
      FragmentManager fragmentManager = getSupportFragmentManager();
      fragmentManager.beginTransaction()
          .replace(R.id.searchContainer, uiSearchBaseContentData)
          .commit();
    } catch (Exception ignored) {
    }
  }

  private void hideKeyboard() {
    View currentFocus = getCurrentFocus();
    if (currentFocus != null) {
      InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
    }
  }
}
