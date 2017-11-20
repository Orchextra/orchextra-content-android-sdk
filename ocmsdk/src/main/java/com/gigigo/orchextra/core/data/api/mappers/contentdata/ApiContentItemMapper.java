package com.gigigo.orchextra.core.data.api.mappers.contentdata;

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.orchextra.core.data.api.dto.content.ApiContentItem;
import com.gigigo.orchextra.core.data.api.dto.elements.ApiElement;
import com.gigigo.orchextra.core.data.api.mappers.elements.ApiElementMapper;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentItem;
import com.gigigo.orchextra.core.domain.entities.elements.Element;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ApiContentItemMapper
    implements ExternalClassToModelMapper<ApiContentItem, ContentItem> {

  private final ApiContentItemLayoutMapper apiContentItemLayoutMapper;
  private final ApiElementMapper apiElementMapper;

  public ApiContentItemMapper(ApiContentItemLayoutMapper apiContentItemLayoutMapper,
      ApiElementMapper apiElementMapper) {
    this.apiContentItemLayoutMapper = apiContentItemLayoutMapper;
    this.apiElementMapper = apiElementMapper;
  }

  @Override public ContentItem externalClassToModel(ApiContentItem data) {
    ContentItem model = new ContentItem();
    model.setSlug(data.getSlug());
    model.setType(data.getType());

    List<String> tagList = new ArrayList<>();
    if (data.getTags() != null) {
      for (String tag : data.getTags()) {
        tagList.add(tag);
      }
    }
    model.setTags(tagList);

    model.setLayout(apiContentItemLayoutMapper.externalClassToModel(data.getLayout()));

    List<Element> elementList = new ArrayList<>();
    if (data.getElements() != null) {
      for (ApiElement apiElement : data.getElements()) {

        if (hasToBeAddedToListBecauseItemIsInDate(apiElement)) {
          Element element = apiElementMapper.externalClassToModel(apiElement);
          if (element != null) {
            elementList.add(element);
          }
        }
      }
    }
    model.setElements(elementList);

    return model;
  }

  private static final String DATE_FORMAT_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

  private boolean hasToBeAddedToListBecauseItemIsInDate(ApiElement apiElement) {
    Calendar calendar = Calendar.getInstance();
    Date calendarTime = calendar.getTime();

    List<List<String>> dates = apiElement.getDates();
    if (dates == null) {
      return true;
    }

    for (List<String> date : dates) {
      if (date.size() == 2) {

        String startTimeString = date.get(0);
        String endTimeString = date.get(1);

        try {
          SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_TIME, Locale.getDefault());

          Date startTime = format.parse(startTimeString);
          Date endTime = format.parse(endTimeString);

          if (startTime.before(calendarTime) && endTime.after(calendarTime)) {
            return true;
          }

        } catch (Exception ignored) {
          ignored.printStackTrace();
        }
      }
    }

    return false;
  }
}
