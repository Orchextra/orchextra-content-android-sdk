package com.gigigo.orchextra.core.sdk.model.detail.viewtypes.cards;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

class CardItemRecyclerViewAdapter<T extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<T> {

  private List<T> items = new ArrayList<>();

  @Override public T onCreateViewHolder(ViewGroup parent, int viewType) {
    return null;
  }

  @Override public void onBindViewHolder(T holder, int position) {

  }

  @Override public int getItemCount() {
    return items.size();
  }

  public void setItems(List<T> items) {
    this.items = items;
  }
}
