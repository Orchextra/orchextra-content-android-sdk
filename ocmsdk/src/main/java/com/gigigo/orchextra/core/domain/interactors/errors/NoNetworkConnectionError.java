package com.gigigo.orchextra.core.domain.interactors.errors;

import com.gigigo.interactorexecutor.interactors.InteractorError;
import com.gigigo.interactorexecutor.responses.BusinessError;

public class NoNetworkConnectionError implements InteractorError {

    @Override
    public BusinessError getError() {
        return BusinessError.createKOInstance("");
    }
}
