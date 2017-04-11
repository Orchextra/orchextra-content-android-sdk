package com.gigigo.orchextra.core.data.api.mappers;

import com.gigigo.interactorexecutor.responses.BusinessError;
import com.gigigo.interactorexecutor.responses.BusinessObject;
import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import java.util.Date;

public class BusinessObjectMapper<Data> {

  public BusinessObject<Data> mapToBusinessObjectFromGggLibToInteractorExecutor(com.gigigo.gggjavalib.business.model.BusinessObject data) {
    BusinessObject<Data> model;
    if (data.isSuccess()) {
      model = new BusinessObject(data.getData(), BusinessError.createOKInstance());
    } else {
      model = new BusinessObject<>(null,
          BusinessError.createKOInstance(data.getBusinessError().getMessage()));
    }

    return model;
  }
}
