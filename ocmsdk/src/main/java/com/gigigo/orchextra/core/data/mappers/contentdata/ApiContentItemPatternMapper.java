package com.gigigo.orchextra.core.data.mappers.contentdata;

import android.util.Log;
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.orchextra.core.data.api.dto.content.ApiContentItemPattern;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItemPattern;

public class ApiContentItemPatternMapper
    implements ExternalClassToModelMapper<ApiContentItemPattern, ContentItemPattern> {

  @Override public ContentItemPattern externalClassToModel(ApiContentItemPattern data) {
    final long time = System.currentTimeMillis();

    ContentItemPattern model = new ContentItemPattern();

    model.setRow(data.getRow());
    model.setColumn(data.getColumn());

    Log.v("TT - ApiContentItemPatt", (System.currentTimeMillis() - time) / 1000 + "");
    return model;
  }
}
