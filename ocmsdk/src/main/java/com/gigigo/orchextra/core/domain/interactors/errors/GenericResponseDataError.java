package com.gigigo.orchextra.core.domain.interactors.errors;

import com.gigigo.interactorexecutor.interactors.InteractorError;
import com.gigigo.interactorexecutor.responses.BusinessError;

public class GenericResponseDataError implements InteractorError {

    private final BusinessError businessError;

    public GenericResponseDataError(BusinessError businessError) {
        this.businessError = businessError;
    }

    @Override
    public BusinessError getError() {
        return businessError;
    }
}
