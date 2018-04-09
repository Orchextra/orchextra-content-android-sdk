package com.gigigo.orchextra.core.data.mappers.contentdata;

import android.util.Log;
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.orchextra.core.data.api.dto.content.ApiContentItemLayout;
import com.gigigo.orchextra.core.data.api.dto.content.ApiContentItemPattern;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItemLayout;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItemPattern;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItemTypeLayout;
import java.util.ArrayList;
import java.util.List;

public class ApiContentItemLayoutMapper
    implements ExternalClassToModelMapper<ApiContentItemLayout, ContentItemLayout> {

  private final ApiContentItemPatternMapper apiContentItemPatternMapper;

  public ApiContentItemLayoutMapper(ApiContentItemPatternMapper apiContentItemPatternMapper) {
    this.apiContentItemPatternMapper = apiContentItemPatternMapper;
  }

  @Override public ContentItemLayout externalClassToModel(ApiContentItemLayout data) {
    final long time = System.currentTimeMillis();

    ContentItemLayout model = new ContentItemLayout();

    model.setName(data.getName());
    model.setType(ContentItemTypeLayout.Companion.convertFromString(data.getType()));

    List<ContentItemPattern> patternList = new ArrayList<>();
    if (data.getPattern() != null) {
      for (ApiContentItemPattern apiPattern : data.getPattern()) {
        ContentItemPattern contentItemPattern =
            apiContentItemPatternMapper.externalClassToModel(apiPattern);

        patternList.add(contentItemPattern);
      }
    }
    model.setPattern(patternList);
    Log.v("TT - ApiContentItemLay", (System.currentTimeMillis() - time) / 1000 + "");

    return model;
  }
}
