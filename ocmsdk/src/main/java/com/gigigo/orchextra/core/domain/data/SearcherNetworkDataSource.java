package com.gigigo.orchextra.core.domain.data;


import com.gigigo.interactorexecutor.responses.BusinessObject;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;

public interface SearcherNetworkDataSource {
  BusinessObject<ContentData> doSearch(String textToSearch);
}
