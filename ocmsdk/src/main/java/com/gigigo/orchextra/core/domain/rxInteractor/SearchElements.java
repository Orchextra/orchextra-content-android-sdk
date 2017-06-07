package com.gigigo.orchextra.core.domain.rxInteractor;

import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.rxExecutor.PostExecutionThread;
import com.gigigo.orchextra.core.domain.rxExecutor.ThreadExecutor;
import com.gigigo.orchextra.core.domain.rxRepository.OcmRepository;
import io.reactivex.Observable;
import orchextra.javax.inject.Inject;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * retrieving a collection of all {@link ContentData}.
 */
public class SearchElements extends UseCase<ContentData, SearchElements.Params> {

  private final OcmRepository ocmRepository;

  @Inject SearchElements(OcmRepository ocmRepository, ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.ocmRepository = ocmRepository;
  }

  @Override Observable<ContentData> buildUseCaseObservable(Params params) {
    return this.ocmRepository.doSearch(params.textToSearch);
  }

  public static final class Params {

    private final boolean forceReload;
    private final String textToSearch;

    private Params(boolean forceReload, String textToSearch) {
      this.forceReload = forceReload;
      this.textToSearch = textToSearch;
    }

    public static Params forTextToSearch(boolean forceReload, String textToSearch) {
      return new Params(forceReload, textToSearch);
    }
  }
}
