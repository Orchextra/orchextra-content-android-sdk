package com.gigigo.orchextra.core.data.api.mappers;

import com.gigigo.gggjavalib.business.model.BusinessContentType;
import com.gigigo.gggjavalib.business.model.BusinessError;
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.ggglib.network.mappers.ApiGenericResponseMapper;
import com.gigigo.ggglib.network.responses.ApiGenericExceptionResponse;
import com.gigigo.orchextra.core.data.api.dto.base.ApiErrorResponse;

public class OcmGenericResponseMapper<Model, Data>
    extends ApiGenericResponseMapper<Model, Data, ApiErrorResponse> {

  public OcmGenericResponseMapper(ExternalClassToModelMapper<Model, Data> mapper) {
    super(mapper);
  }

  @Override
  protected BusinessError createBusinessError(ApiErrorResponse apiErrorResponse, Data result) {

    if (apiErrorResponse != null) {
      int code = apiErrorResponse.getCode();
      String message = apiErrorResponse.getMessage();
      return new BusinessError(code, message, BusinessContentType.BUSINESS_ERROR_CONTENT);
    }

    return BusinessError.createKoInstance("Empty error message");
  }

  @Override protected BusinessError onException(ApiGenericExceptionResponse exceptionResponse) {
    int code = BusinessError.EXCEPTION_BUSINESS_ERROR_CODE;
    String message = exceptionResponse.getBusinessError().getMessage();
    BusinessError businessError =
        new BusinessError(code, message, BusinessContentType.EXCEPTION_CONTENT);
    return businessError;
  }
}

