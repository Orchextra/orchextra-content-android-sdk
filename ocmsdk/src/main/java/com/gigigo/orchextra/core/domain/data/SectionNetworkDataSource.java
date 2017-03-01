package com.gigigo.orchextra.core.domain.data;

import com.gigigo.interactorexecutor.responses.BusinessObject;

public interface SectionNetworkDataSource {
  BusinessObject getSectionData(String section);
}
